use master
go

-- PHƯƠNG THỨC XÓA DATABASE (dùng cho việc xóa cài lại)
--use master
--DROP DATABASE HotelDatabase



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
VALUES 
('CS-000001', 'Dịch vụ Khách sạn'),  -- Cập nhật tên loại dịch vụ
('CS-000002', 'Dịch vụ Spa'),         -- Cập nhật tên loại dịch vụ
('CS-000003', 'Dịch vụ Hội nghị'),     -- Cập nhật tên loại dịch vụ
('CS-000004', 'Dịch vụ Đón sân bay');  -- Cập nhật tên loại dịch vụ

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
('HS-000001', 'Dịch vụ phòng', 'Dịch vụ phòng 24/7 cho tất cả khách', 50.00, 'CS-000001'),
('HS-000002', 'Dịch vụ Spa', 'Massage toàn thân và liệu trình spa', 120.00, 'CS-000002'),
('HS-000003', 'Đặt phòng hội nghị', 'Đặt phòng hội nghị cho các cuộc họp', 200.00, 'CS-000003'),
('HS-000004', 'Dịch vụ đón sân bay', 'Dịch vụ xe sang cho việc đón sân bay', 75.00, 'CS-000004');

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
('RUS-000001', 2, 'HS-000001'),  -- Dịch vụ phòng
('RUS-000002', 1, 'HS-000002'),  -- Dịch vụ Spa
('RUS-000003', 3, 'HS-000003'),  -- Đặt phòng hội nghị
('RUS-000004', 1, 'HS-000004');  -- Dịch vụ đón sân bay

Go

--Tạo bảng Customer
CREATE TABLE Customer (
    customerID VARCHAR(10) NOT NULL PRIMARY KEY,
    fullName NVARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(10),
    email VARCHAR(50) NOT NULL,
    address NVARCHAR(100) NOT NULL,
    gender VARCHAR(6) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    idCardNumber VARCHAR(12) NOT NULL,
    dob DATE NOT NULL
)

--Thêm dữ liệu vào bảng Custormer
INSERT INTO Customer (customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob)
VALUES 
('CUS-000001', N'Nguyễn Văn A', '0912345678', 'nguyenvana@gmail.com', N'123 Đường ABC, Quận 1, TP HCM', 'MALE', '123456789012', '1990-05-15'),
('CUS-000002', N'Lê Thị B', '0912345679', 'lethib@gmail.com', N'456 Đường XYZ, Quận 3, TP HCM', 'FEMALE', '987654321012', '1992-07-22'),
('CUS-000003', N'Trần Văn C', '0912345680', 'tranvanc@gmail.com', N'789 Đường MNO, Quận 5, TP HCM', 'MALE', '345678901234', '1988-03-30'),
('CUS-000004', N'Phạm Thị D', '0912345681', 'phamthid@gmail.com', N'321 Đường PQR, Quận 7, TP HCM', 'FEMALE', '567890123456', '1995-12-01'),
('CUS-000005', N'Hoàng Văn E', '0912345682', 'hoangvane@gmail.com', N'987 Đường STU, Quận 10, TP HCM', 'MALE', '678901234567', '1991-11-20'),
('CUS-000006', N'Đỗ Thị F', '0912345683', 'dothif@gmail.com', N'654 Đường VWX, Quận 9, TP HCM', 'FEMALE', '789012345678', '1993-04-18'),
('CUS-000007', N'Vũ Văn G', '0912345684', 'vuvang@gmail.com', N'321 Đường YZ, Quận Bình Thạnh, TP HCM', 'MALE', '890123456789', '1985-02-25'),
('CUS-000008', N'Nguyễn Thị H', '0912345685', 'nguyenthih@gmail.com', N'543 Đường LMN, Quận Phú Nhuận, TP HCM', 'FEMALE', '901234567890', '1990-08-14'),
('CUS-000009', N'Phan Văn I', '0912345686', 'phanvani@gmail.com', N'876 Đường QRS, Quận 2, TP HCM', 'MALE', '012345678901', '1994-09-09'),
('CUS-000010', N'Trịnh Thị K', '0912345687', 'trinhthik@gmail.com', N'123 Đường OPQ, Quận Gò Vấp, TP HCM', 'FEMALE', '234567890123', '1996-06-06');
