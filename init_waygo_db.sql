-- =============================================================================
-- BAKI AĞILLI HƏRƏKƏTLİLİK SİSTEMİ (WAYGO) - POSTGRESQL DATABASE INITIALIZATION
-- Database: waygo
-- =============================================================================

-- 1. Create Database (Run this line in postgres database if waygo does not exist)
-- CREATE DATABASE waygo;

-- 2. Connect to waygo database before creating tables:
-- \c waygo;

-- Enable UUID extension for auto UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =============================================================================
-- TABLE 1: ROAD SEGMENT (Yol Seqmentləri)
-- =============================================================================
CREATE TABLE IF NOT EXISTS road_segment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    coordinates_json TEXT NOT NULL,
    zone VARCHAR(100) NOT NULL
);

-- =============================================================================
-- TABLE 2: TRAFFIC SNAPSHOT (Anlıq Tıxac Göstəriciləri)
-- =============================================================================
CREATE TABLE IF NOT EXISTS traffic_snapshot (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    segment_id UUID NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    avg_speed DOUBLE PRECISION NOT NULL,
    congestion_level INT NOT NULL,
    CONSTRAINT fk_snapshot_segment FOREIGN KEY (segment_id) REFERENCES road_segment(id) ON DELETE CASCADE
);

-- =============================================================================
-- TABLE 3: HISTORICAL PATTERN (Həftəlik Və Saatlıq Statistik Modellər)
-- =============================================================================
CREATE TABLE IF NOT EXISTS historical_pattern (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    segment_id UUID NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    hour INT NOT NULL CHECK (hour >= 0 AND hour <= 23),
    avg_speed DOUBLE PRECISION NOT NULL,
    std_dev DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_pattern_segment FOREIGN KEY (segment_id) REFERENCES road_segment(id) ON DELETE CASCADE
);

-- =============================================================================
-- TABLE 4: ANOMALY LOG (Z-Score Sürət Krizləri Və Anomaliyalar)
-- =============================================================================
CREATE TABLE IF NOT EXISTS anomaly_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    segment_id UUID NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    z_score DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    description TEXT,
    CONSTRAINT fk_anomaly_segment FOREIGN KEY (segment_id) REFERENCES road_segment(id) ON DELETE CASCADE
);

-- =============================================================================
-- TABLE 5: USER REPORT (Sürücü Qəza Və Təmir Hadisələri)
-- =============================================================================
CREATE TABLE IF NOT EXISTS user_report (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    segment_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_segment FOREIGN KEY (segment_id) REFERENCES road_segment(id) ON DELETE CASCADE
);

-- =============================================================================
-- TABLE 6: GPS PING (Canlı GPS Akselerometr Və Koordinat Axını)
-- =============================================================================
CREATE TABLE IF NOT EXISTS gps_ping (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_id VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    speed_kmh DOUBLE PRECISION NOT NULL
);

-- =============================================================================
-- INITIAL SEED DATA FOR BAKU ROADS & ANOMALIES
-- =============================================================================
INSERT INTO road_segment (id, name, coordinates_json, zone) VALUES
('11111111-1111-1111-1111-111111111111', 'Heydar Aliyev Avenue', '[{"latitude":40.4093,"longitude":49.8671},{"latitude":40.4084,"longitude":49.8756}]', 'Nizami'),
('22222222-2222-2222-2222-222222222222', 'Koroglu Metro Corridor', '[{"latitude":40.4012,"longitude":49.8765},{"latitude":40.4031,"longitude":49.8902}]', 'Binagadi'),
('33333333-3333-3333-3333-333333333333', 'Ziya Bunyadov', '[{"latitude":40.4318,"longitude":49.8501},{"latitude":40.4234,"longitude":49.8625}]', 'Yasamal'),
('44444444-4444-4444-4444-444444444444', 'Nobel Prospect', '[{"latitude":40.3728,"longitude":49.8768},{"latitude":40.3652,"longitude":49.8824}]', 'Khatai'),
('55555555-5555-5555-5555-555555555555', '28 May Corridor', '[{"latitude":40.3798,"longitude":49.8438},{"latitude":40.3821,"longitude":49.8521}]', 'Sabayil')
ON CONFLICT (id) DO NOTHING;

INSERT INTO anomaly_log (segment_id, z_score, status, description) VALUES
('11111111-1111-1111-1111-111111111111', -2.84, 'ACTIVE', 'Heydər Əliyev prospektində Z-Score ani sürət düşümü'),
('22222222-2222-2222-2222-222222222222', -2.31, 'ACTIVE', 'Koroğlu Metro dəhlizində pik tıxac anomaliyası'),
('33333333-3333-3333-3333-333333333333', -2.15, 'ACTIVE', 'Ziya Bünyadov prospektində ləng hərəkət axını');
