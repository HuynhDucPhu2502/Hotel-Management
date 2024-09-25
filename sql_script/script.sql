use master
go

-- PHƯƠNG THỨC XÓA DATABASE (dùng cho việc xóa cài lại)
use master
DROP DATABASE HotelDatabase



-- Tạo cơ sở dữ liệu HotelManagement
CREATE DATABASE HotelDatabase
GO

-- Sử dụng cơ sở dữ liệu HotelManagement
USE HotelDatabase
GO

-- Tạo bảng Employee
CREATE TABLE Employee (
    employeeID VARCHAR(10) NOT NULL PRIMARY KEY, 
	-- Mã nhân viên
    fullName NVARCHAR(50) NOT NULL, 
	-- Họ tên nhân viên
    phoneNumber VARCHAR(10) NOT NULL, 
	-- Số điện thoại
    email VARCHAR(50) NOT NULL, 
	-- Email nhân viên
    address NVARCHAR(100), 
	-- Địa chỉ
    gender VARCHAR(6) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')), 
	-- Giới tính với ràng buộc enum
    idCardNumber VARCHAR(12) NOT NULL, 
	-- Số CCCD/CMND
    dob DATE NOT NULL, 
	-- Ngày sinh
    position VARCHAR(15) NOT NULL CHECK (position IN ('RECEPTIONIST', 'MANAGER')) 
	-- Chức vụ với ràng buộc enum
)
GO

-- Thêm dữ liệu vào bảng Employee
INSERT INTO Employee (employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position)
VALUES 
('EMP-000001', 'Huynh Duc Phu', '0912345678', 'phuhuynh@gmail.com', '123 Ho Chi Minh', 'MALE', '001099012345', '1985-06-15', 'MANAGER'),
('EMP-000002', 'Nguyen Xuan Chuc', '0908765432', 'chucnguyen@yahoo.com', '456 Hue', 'MALE', '002199012346', '1990-04-22', 'RECEPTIONIST'),
('EMP-000003', 'Le Tran Gia Huy', '0987654321', 'huytranle@gmail.com', '789 Ho Chi Minh', 'MALE', '003299012347', '1992-08-19', 'MANAGER'),
('EMP-000004', 'Dang Nguyen Tien Phat', '0934567890', 'phatnguyen@gmail.com', '321 Binh Phuoc', 'MALE', '004399012348', '1987-12-05', 'RECEPTIONIST'),
('EMP-000005', 'Vu Ba Hai', '0923456789', 'vubachai@yahoo.com', '654 Long an', 'MALE', '004399012349', '1995-03-30', 'MANAGER');

-- Tạo bảng Shift
CREATE TABLE Shift (
    shiftID VARCHAR(15) NOT NULL PRIMARY KEY, 
	-- Mã ca
    startTime TIME NOT NULL, 
	-- Thời gian bắt đầu ca
    endTime TIME NOT NULL, 
	-- Thời gian kết thúc ca
    modifiedDate DATETIME NOT NULL, 
	-- Lần cuối cập nhật ca làm
    numberOfHour FLOAT NOT NULL, 
	-- Thời gian làm việc theo giờ
    shiftDaysSchedule VARCHAR(20) NOT NULL CHECK (shiftDaysSchedule IN ('MON_WEB_FRI', 'TUE_THU_SAT', 'SUNDAY')) 
	-- Phải nằm trong các giá trị MON_WEB_FRI, TUE_THU_SAT, SUNDAY
);


-- Thêm dữ liệu vào bảng Shift
INSERT INTO Shift (shiftID, startTime, endTime, modifiedDate, numberOfHour, shiftDaysSchedule)
VALUES
-- Ca làm cho MON_WEB_FRI
('SHIFT-AM-0001', '05:00', '11:00', GETDATE(), 6, 'MON_WEB_FRI'),
('SHIFT-AM-0002', '11:00', '17:00', GETDATE(), 6, 'MON_WEB_FRI'),
('SHIFT-PM-0003', '17:00', '23:00', GETDATE(), 6, 'MON_WEB_FRI'),
-- Ca làm cho TUE_THU_SAT
('SHIFT-AM-0004', '05:00', '11:00', GETDATE(), 6, 'TUE_THU_SAT'),
('SHIFT-AM-0005', '11:00', '17:00', GETDATE(), 6, 'TUE_THU_SAT'),
('SHIFT-PM-0006', '17:00', '23:00', GETDATE(), 6, 'TUE_THU_SAT'),
-- Ca làm cho SUNDAY
('SHIFT-AM-0007', '05:00', '11:00', GETDATE(), 6, 'SUNDAY'),
('SHIFT-AM-0008', '11:00', '17:00', GETDATE(), 6, 'SUNDAY'),
('SHIFT-PM-0009', '17:00', '23:00', GETDATE(), 6, 'SUNDAY');


Go

--Tạo bảo CategoryService
CREATE TABLE CategoryService (
    serviceCategoryID VARCHAR(10) NOT NULL PRIMARY KEY,
    serviceCategoryName NVARCHAR(50) NOT NULL
)

Go 
--Thêm dữ liệu vào bảng CategoryService
INSERT INTO CategoryService (serviceCategoryID, serviceCategoryName)
VALUES ('CS-000001', 'IT Services'),
       ('CS-000002', 'Healthcare Services'),
       ('CS-000003', 'Financial Services'),
       ('CS-000004', 'Education Services');

Go
--Tạo bảo HotelService
CREATE TABLE HotelService (
    hotelServiceId VARCHAR(10) NOT NULL PRIMARY KEY,
    serviceName NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NOT NULL,
    servicePrice MONEY NOT NULL,
    serviceCategoryID VARCHAR(10) NOT NULL,
    FOREIGN KEY (serviceCategoryID) REFERENCES CategoryService(serviceCategoryID)
)

-- Thêm dữ liệu vào bảng HotelService
INSERT INTO HotelService (hotelServiceId, serviceName, description, servicePrice, serviceCategoryID)
VALUES 
('HS-000001', 'Room Service', '24/7 room service for all guests', 50.00, 'CS-000001'),
('HS-000002', 'Spa Service', 'Full body massage and spa treatments', 120.00, 'CS-000002'),
('HS-000003', 'Conference Room Booking', 'Booking of conference room for meetings', 200.00, 'CS-000003'),
('HS-000004', 'Airport Pickup', 'Luxury car service for airport transfers', 75.00, 'CS-000004');

--Tạo bảng RoomUsageService
CREATE TABLE RoomUsageService (
    roomUsageServiceId VARCHAR(10) NOT NULL PRIMARY KEY,
    quantity INT NOT NULL,
    hotelServiceId VARCHAR(10) NOT NULL,
    FOREIGN KEY (hotelServiceId) REFERENCES HotelService(hotelServiceId)
)

--Thêm dữ liệu vào bảng RoomUsageService
INSERT INTO RoomUsageService (roomUsageServiceId, quantity, hotelServiceId)
VALUES 
('RUS-000001', 2, 'HS-000001'),  -- Room Service
('RUS-000002', 1, 'HS-000002'),  -- Spa Service
('RUS-000003', 3, 'HS-000003'),  -- Conference Room Booking
('RUS-000004', 1, 'HS-000004');  -- Airport Pickup