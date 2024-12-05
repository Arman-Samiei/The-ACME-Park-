-- Insert mock data for LotData
INSERT INTO spots (SPOT_ID, LOT_ID, CUSTOMER_TYPE, PLATE_NUMBER, IS_SPOT_OCCUPIED, SPOT_RESERVATION_STATUS,
                   HAS_VOUCHER)
VALUES
-- LOT01
('SPOT101', 'LOT01', 'visitor', 'ABC123', true, 'reserved', true),   -- Visitor with voucher
('SPOT102', 'LOT01', 'visitor', NULL, false, 'notReserved', false),    -- Free spot for voucher/non-voucher visitors
('SPOT103', 'LOT01', 'student', 'DEF456', true, 'reserved', false), -- Reserved for student
('SPOT104', 'LOT01', 'faculty', NULL, false, 'notReserved', false),   -- Free spot for faculty
('SPOT105', 'LOT01', 'visitor', NULL, false, 'notReserved', false),     -- Free spot for voucher/non-voucher visitors

-- LOT02
('SPOT201', 'LOT02', 'staff', 'GHI789', false, 'reserved', false),   -- Reserved for staff
('SPOT202', 'LOT02', 'visitor', NULL, false, 'notReserved', false),    -- Free spot for voucher/non-voucher visitors
('SPOT203', 'LOT02', 'visitor', 'JKL012', false, 'reserved', true),   -- Reserved for visitor with voucher
('SPOT204', 'LOT02', 'faculty', 'MNO345', true, 'reserved', false), -- Reserved for faculty
('SPOT205', 'LOT02', 'visitor', NULL, false, 'notReserved', false),    -- Free spot for voucher/non-voucher visitors

-- LOT03
('SPOT301', 'LOT03', 'visitor', 'PQR678', true, 'reserved', true),   -- Visitor with voucher
('SPOT302', 'LOT03', 'student', NULL, false, 'notReserved', false),   -- Free spot for student
('SPOT303', 'LOT03', 'faculty', 'STU901', true, 'reserved', false), -- Reserved for faculty
('SPOT304', 'LOT03', 'visitor', NULL, false, 'notReserved', false),     -- Free spot for voucher/non-voucher visitors
('SPOT305', 'LOT03', 'staff', 'VWX234', true, 'reserved', false),   -- Reserved for staff

-- LOT04
('SPOT401', 'LOT04', 'visitor', 'YZA567', true, 'reserved', false),  -- Visitor without voucher
('SPOT402', 'LOT04', 'faculty', NULL, false, 'notReserved', false),   -- Free spot for faculty
('SPOT403', 'LOT04', 'visitor', NULL, false, 'notReserved', false),    -- Free spot for voucher/non-voucher visitors
('SPOT404', 'LOT04', 'student', 'BCD890', false, 'reserved', false), -- Reserved for student
('SPOT405', 'LOT04', 'visitor', 'EFG123', false, 'reserved', true),   -- Visitor with voucher

-- LOT05
('SPOT501', 'LOT05', 'staff', NULL, false, 'notReserved', false),     -- Free spot for staff
('SPOT502', 'LOT05', 'visitor', 'HIJ456', true, 'reserved', true),   -- Visitor with voucher
('SPOT503', 'LOT05', 'faculty', 'KLM789', false, 'reserved', false), -- Reserved for faculty
('SPOT504', 'LOT05', 'visitor', NULL, false, 'notReserved', false),    -- Free spot for voucher/non-voucher visitors
('SPOT505', 'LOT05', 'student', 'NOP012', false, 'reserved', false); -- Reserved for student
