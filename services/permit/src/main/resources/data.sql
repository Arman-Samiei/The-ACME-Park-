-- Insert mock data for PermitData
INSERT INTO permit (TRANSPONDER_ID, PLATE_NUMBER, FIRST_NAME, LAST_NAME, EMPLOYEE_ID, LOT_ID, SPOT_ID, STATUS,
                         MEMBER_PAYMENT_TYPE, MEMBER_ROLE, CC_NUMBER, CC_EXPIRY, CC_CVC, MONTHS_PURCHASED,
                         EXPIRATION_TIME)
VALUES
-- Reserved spots for student, faculty, and staff
-- LOT01
('s1234567890', 'DEF456', 'Talha', 'Asif', 'E12345', 'LOT01', 'SPOT103', 'issued', 'bank', 'student',
 '4111111111111111', '12/25', '123', 12, '2024-12-31 23:59:59'),

-- LOT02
('m9876543210', 'GHI789', 'Jane', 'Smith', 'S67890', 'LOT02', 'SPOT201', 'issued', 'payslip', 'staff',
 '4222222222222222', '06/26', '456', 24, '2025-06-30 23:59:59'),

-- LOT03
('m1230984567', 'VWX234', 'Robert', 'Brown', 'S56789', 'LOT03', 'SPOT305', 'issued', 'bank', 'staff',
 '4333333333333333', '11/27', '789', 18, '2024-11-30 23:59:59'),

-- LOT04
('s2345678901', 'BCD890', 'Maryam', 'Emami', 'E34567', 'LOT04', 'SPOT404', 'issued', 'bank', 'student',
 '4444444444444444', '03/28', '012', 12, '2024-03-31 23:59:59'),

-- LOT05
('s3456789012', 'NOP012', 'Arman', 'Samiei', 'E45678', 'LOT05', 'SPOT505', 'issued', 'bank', 'student',
 '5555555555555555', '09/27', '345', 12, '2024-09-30 23:59:59'),
('f1234567890', 'KLM789', 'Sebastien', 'Mosser', 'F56789', 'LOT05', 'SPOT503', 'issued', 'payslip', 'faculty',
 '1666666666666666', '10/27', '678', 24, '2025-10-31 23:59:59');
