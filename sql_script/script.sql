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
