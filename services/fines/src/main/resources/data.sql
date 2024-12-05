-- Insert mock data for fines
INSERT INTO fines (PLATE_NUMBER, AMOUNT)
VALUES
-- Fines for reserved student, faculty, and staff spots
('DEF456', 50.00), -- Fine for student
('MNO345', 75.00), -- Fine for faculty
('VWX234', 30.00), -- Fine for staff

-- Fines for visitor with voucher and occupied spot
('ABC123', 100.00), -- Fine for visitor with voucher
('JKL012', 80.00),  -- Fine for visitor with voucher

-- Fines for visitor without voucher and occupied spot
('YZA567', 60.00), -- Fine for visitor without voucher
('HIJ456', 90.00)  -- Fine for visitor without voucher
;