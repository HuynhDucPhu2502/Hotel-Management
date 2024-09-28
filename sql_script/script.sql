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
('SHIFT-PM-0002', '11:00', '17:00', GETDATE(), 6, 'MON_WEB_FRI'),
('SHIFT-PM-0003', '17:00', '23:00', GETDATE(), 6, 'MON_WEB_FRI'),
-- Ca làm cho TUE_THU_SAT
('SHIFT-AM-0004', '05:00', '11:00', GETDATE(), 6, 'TUE_THU_SAT'),
('SHIFT-PM-0005', '11:00', '17:00', GETDATE(), 6, 'TUE_THU_SAT'),
('SHIFT-PM-0006', '17:00', '23:00', GETDATE(), 6, 'TUE_THU_SAT'),
-- Ca làm cho SUNDAY
('SHIFT-AM-0007', '05:00', '11:00', GETDATE(), 6, 'SUNDAY'),
('SHIFT-PM-0008', '11:00', '17:00', GETDATE(), 6, 'SUNDAY'),
('SHIFT-PM-0009', '17:00', '23:00', GETDATE(), 6, 'SUNDAY');


Go

--Tạo bảo ServiceCategory
CREATE TABLE ServiceCategory (
    serviceCategoryID VARCHAR(10) NOT NULL PRIMARY KEY,
    serviceCategoryName NVARCHAR(50) NOT NULL
)

Go
--Thêm dữ liệu vào bảng ServiceCategory
INSERT INTO ServiceCategory (serviceCategoryID, serviceCategoryName)
VALUES
('SC-000001', 'Dich vu Khach san'),  -- Cap nhat ten loai dich vu
('SC-000002', 'Dich vu Spa'),        -- Cap nhat ten loai dich vu
('SC-000003', 'Dich vu Hoi nghi'),   -- Cap nhat ten loai dich vu
('SC-000004', 'Dich vu Don san bay'); -- Cap nhat ten loai dich vu


Go
--Tạo bảo HotelService
CREATE TABLE HotelService (
    hotelServiceId VARCHAR(10) NOT NULL PRIMARY KEY,
    serviceName NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NOT NULL,
    servicePrice MONEY NOT NULL,
    serviceCategoryID VARCHAR(10) NOT NULL,
    FOREIGN KEY (serviceCategoryID) REFERENCES ServiceCategory(serviceCategoryID)
)

-- Thêm dữ liệu vào bảng HotelService
INSERT INTO HotelService (hotelServiceId, serviceName, description, servicePrice, serviceCategoryID)
VALUES
('HS-000001', 'Dich vu phong', 'Dich vu phong 24/7 cho tat ca khach', 50.00, 'SC-000001'),
('HS-000002', 'Dich vu Spa', 'Massage toan than va lieu trinh spa', 120.00, 'SC-000002'),
('HS-000003', 'Dat phong hoi nghi', 'Dat phong hoi nghi cho cac cuoc hop', 200.00, 'SC-000003'),
('HS-000004', 'Dich vu don san bay', 'Dich vu xe sang cho viec don san bay', 75.00, 'SC-000004');


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
('CUS-000001', N'Nguyen Van A', '0912345678', 'nguyenvana@gmail.com', N'123 Duong ABC, Quan 1, TP HCM', 'MALE', '001099012333', '1990-05-15'),
('CUS-000002', N'Le Thi B', '0912345679', 'lethib@gmail.com', N'456 Duong XYZ, Quan 3, TP HCM', 'FEMALE', '001099012323', '1992-07-22'),
('CUS-000003', N'Tran Van C', '0912345680', 'tranvanc@gmail.com', N'789 Duong MNO, Quan 5, TP HCM', 'MALE', '001099012343', '1988-03-30'),
('CUS-000004', N'Pham Thi D', '0912345681', 'phamthid@gmail.com', N'321 Duong PQR, Quan 7, TP HCM', 'FEMALE', '001099012546', '1995-12-01'),
('CUS-000005', N'Hoang Van E', '0912345682', 'hoangvane@gmail.com', N'987 Duong STU, Quan 10, TP HCM', 'MALE', '001099012764', '1991-11-20'),
('CUS-000006', N'Do Thi F', '0912345683', 'dothif@gmail.com', N'654 Duong VWX, Quan 9, TP HCM', 'FEMALE', '001099012654', '1993-04-18'),
('CUS-000007', N'Vu Van G', '0912345684', 'vuvang@gmail.com', N'321 Duong YZ, Quan Binh Thanh, TP HCM', 'MALE', '001099012768', '1985-02-25'),
('CUS-000008', N'Nguyen Thi H', '0912345685', 'nguyenthih@gmail.com', N'543 Duong LMN, Quan Phu Nhuan, TP HCM', 'FEMALE', '001099012859', '1990-08-14'),
('CUS-000009', N'Phan Van I', '0912345686', 'phanvani@gmail.com', N'876 Duong QRS, Quan 2, TP HCM', 'MALE', '001099012978', '1994-09-09'),
('CUS-000010', N'Trinh Thi K', '0912345687', 'trinhthik@gmail.com', N'123 Duong OPQ, Quan Go Vap, TP HCM', 'FEMALE', '001099012234', '1996-06-06');

go

-- Tạo bảng Account
CREATE TABLE Account (
    accountID NVARCHAR(10) PRIMARY KEY,  
    -- Mã tài khoản
    userName NVARCHAR(20) NOT NULL,  
    -- Tên đăng nhập
    password NVARCHAR(30) NOT NULL,  
    -- Mật khẩu
    status NVARCHAR(10) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED')),  
    -- Trạng thái tài khoản
    employeeID VARCHAR(10) NOT NULL,  
    -- Mã nhân viên
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)  
    -- Liên kết đến bảng Employee
);

-- Thêm dữ liệu vào bảng Account
INSERT INTO Account (accountID, userName, password, status, employeeID)
VALUES 
('ACC-000001', 'huynhducphu', 'test123@', 'ACTIVE', 'EMP-000001'),
('ACC-000002', 'nguyenxuanchuc', 'test123@', 'ACTIVE', 'EMP-000002'),
('ACC-000003', 'tranlegiahuy', 'test123@', 'ACTIVE', 'EMP-000003'),
('ACC-000004', 'dangnguyentienphat', 'test123@', 'ACTIVE', 'EMP-000004'),
('ACC-000005', 'vubahai', 'test123@', 'ACTIVE', 'EMP-000005');


