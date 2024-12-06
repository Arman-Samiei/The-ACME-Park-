-- Insert mock data for fines
INSERT INTO fines (FINES_ID, PLATE_NUMBER, AMOUNT)
VALUES
-- Fines for reserved student, faculty, and staff spots
(70001, 'DEF456', 50.00),  -- Fine for student
(70002, 'MNO345', 75.00),  -- Fine for faculty
(70003, 'VWX234', 30.00),  -- Fine for staff

-- Fines for visitor with voucher and occupied spot
(70004, 'ABC123', 100.00), -- Fine for visitor with voucher
(70005, 'JKL012', 80.00),  -- Fine for visitor with voucher

-- Fines for visitor without voucher and occupied spot
(70006, 'YZA567', 60.00),  -- Fine for visitor without voucher
(70007, 'HIJ456', 90.00) -- Fine for visitor without voucher
;