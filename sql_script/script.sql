﻿USE master;
GO

-- Tạo cơ sở dữ liệu HotelDatabase
CREATE DATABASE HotelDatabase;
GO

-- Sử dụng cơ sở dữ liệu HotelDatabase
USE HotelDatabase;
GO

-- Xóa cơ sở dữ liệu
--USE master
--DROP DATABASE HotelDatabase

-- ===================================================================================
-- 1. TẠO BẢNG
-- ===================================================================================

-- Tạo bảng GlobalSequence
CREATE TABLE GlobalSequence (
    tableName NVARCHAR(50) PRIMARY KEY, 
    nextID NVARCHAR(20)                         
);
GO

-- Tạo bảng Value count
create table ValueCount(
	vName nvarchar(50) primary key,
	vCount int
);

-- Tạo bảng Employee
CREATE TABLE Employee (
    employeeID NVARCHAR(15) NOT NULL PRIMARY KEY, 
    fullName NVARCHAR(50) NOT NULL, 
    phoneNumber NVARCHAR(10) NOT NULL, 
    email NVARCHAR(50) NOT NULL, 
    address NVARCHAR(100), 
    gender NVARCHAR(6) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')), 
    idCardNumber NVARCHAR(12) NOT NULL, 
    dob DATE NOT NULL, 
    position NVARCHAR(15) NOT NULL CHECK (position IN ('RECEPTIONIST', 'MANAGER')) 
);
GO

-- Tạo bảng Account
CREATE TABLE Account (
    accountID NVARCHAR(15) PRIMARY KEY,  
    userName NVARCHAR(20) NOT NULL,  
    password NVARCHAR(30) NOT NULL,  
    status NVARCHAR(10) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED')),  
    employeeID NVARCHAR(15) NOT NULL,  
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)  
);
GO

-- Tạo bảng ServiceCategory
CREATE TABLE ServiceCategory (
    serviceCategoryID NVARCHAR(15) NOT NULL PRIMARY KEY,
    serviceCategoryName NVARCHAR(50) NOT NULL
);
GO

-- Tạo bảng HotelService 
CREATE TABLE HotelService (
    hotelServiceId NVARCHAR(15) NOT NULL PRIMARY KEY,
    serviceName NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NOT NULL,
    servicePrice MONEY NOT NULL,
    serviceCategoryID NVARCHAR(15) NULL,  -- Cho phép NULL

    CONSTRAINT FK_HotelService_ServiceCategory
        FOREIGN KEY (serviceCategoryID) 
        REFERENCES ServiceCategory(serviceCategoryID)
        ON DELETE SET NULL
		ON UPDATE CASCADE,
);
GO

-- Tạo bảng RoomCategory
CREATE TABLE RoomCategory (
    roomCategoryID NVARCHAR(15) NOT NULL PRIMARY KEY, 
    roomCategoryName NVARCHAR(50) NOT NULL, 
    numberOfBed INT NOT NULL
);
GO

-- Tạo bảng Pricing
CREATE TABLE Pricing (
    pricingID NVARCHAR(15) NOT NULL PRIMARY KEY,  
    priceUnit NVARCHAR(15) NOT NULL CHECK (priceUnit IN ('DAY', 'HOUR')),  
    price MONEY NOT NULL,  
    roomCategoryID NVARCHAR(15) NOT NULL,
    
    CONSTRAINT FK_Pricing_RoomCategory FOREIGN KEY (roomCategoryID) 
        REFERENCES RoomCategory(roomCategoryID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    CONSTRAINT UQ_roomCategoryID_priceUnit UNIQUE (roomCategoryID, priceUnit)
);
GO


-- Tạo bảng Room
CREATE TABLE Room (
    roomID NVARCHAR(15) NOT NULL PRIMARY KEY,  
    roomStatus NVARCHAR(20) NOT NULL CHECK (roomStatus IN ('AVAILABLE', 'ON_USE', 'UNAVAILABLE')),  
    dateOfCreation DATETIME NOT NULL,  
    roomCategoryID NVARCHAR(15) NOT NULL,  
    FOREIGN KEY (roomCategoryID) REFERENCES RoomCategory(roomCategoryID)  
);
GO

-- Tạo bảng Shift
CREATE TABLE Shift (
    shiftID NVARCHAR(15) NOT NULL PRIMARY KEY, 
    startTime TIME NOT NULL, 
    endTime TIME NOT NULL, 
    modifiedDate DATETIME NOT NULL, 
    numberOfHour FLOAT NOT NULL, 
    shiftDaysSchedule NVARCHAR(20) NOT NULL CHECK (shiftDaysSchedule IN ('MON_WEB_FRI', 'TUE_THU_SAT', 'SUNDAY')) 
);
GO

-- Tạo bảng ShiftAssignment
CREATE TABLE ShiftAssignment (
    shiftAssignmentId NVARCHAR(15) NOT NULL PRIMARY KEY, 
    description NVARCHAR(50), 
    shiftId NVARCHAR(15) NOT NULL, 
    employeeId NVARCHAR(15) NOT NULL, 
    FOREIGN KEY (shiftId) REFERENCES Shift(shiftID), 
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeID)
);
GO

-- Tạo bảng Customer
CREATE TABLE Customer (
    customerID NVARCHAR(15) NOT NULL PRIMARY KEY,
    fullName NVARCHAR(50) NOT NULL,
    phoneNumber NVARCHAR(10),
    email NVARCHAR(50) NOT NULL,
    address NVARCHAR(100) NOT NULL,
    gender NVARCHAR(6) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    idCardNumber NVARCHAR(12) NOT NULL UNIQUE,
    dob DATE NOT NULL
);
GO


-- Tạo bảng ReservationForm
CREATE TABLE ReservationForm (
    reservationFormID NVARCHAR(15) NOT NULL PRIMARY KEY,        
    reservationDate DATETIME NOT NULL,            
    checkInDate DATETIME NOT NULL,
    checkOutDate DATETIME NOT NULL,
    employeeID NVARCHAR(15),
    roomID NVARCHAR(15),
    customerID NVARCHAR(15),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID) ON DELETE SET NULL,
    FOREIGN KEY (roomID) REFERENCES Room(roomID) ON DELETE SET NULL,
    FOREIGN KEY (customerID) REFERENCES Customer(customerID) ON DELETE SET NULL
);
GO

-- Tạo bảng RoomUsageService
CREATE TABLE RoomUsageService (
    roomUsageServiceId NVARCHAR(15) NOT NULL PRIMARY KEY,  
    quantity INT NOT NULL,  
    hotelServiceId NVARCHAR(15) NOT NULL,  
    reservationFormID NVARCHAR(15) NOT NULL,  
    FOREIGN KEY (hotelServiceId) REFERENCES HotelService(hotelServiceId),  
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  
);
GO

-- Tạo bảng Tax
CREATE TABLE Tax (
    taxID NVARCHAR(15) NOT NULL PRIMARY KEY,          
    taxName NVARCHAR(50) NOT NULL,                  
    taxRate DECIMAL(5, 2) NOT NULL,                          
    dateOfCreation DATE NOT NULL,                    
    activate BIT NOT NULL                             
);
GO

-- Tạo bảng Invoice
CREATE TABLE Invoice (
    invoiceID NVARCHAR(15) NOT NULL PRIMARY KEY,        
    invoiceDate DATETIME NOT NULL,                     
    roomCharge DECIMAL(18, 2) NOT NULL,                
    servicesCharge DECIMAL(18, 2) NOT NULL,            
    totalDue DECIMAL(18, 2) NOT NULL,                  
    netDue DECIMAL(18, 2) NOT NULL,                    
    taxID NVARCHAR(15) NOT NULL,                         
    reservationFormID NVARCHAR(15) NOT NULL,             
    FOREIGN KEY (taxID) REFERENCES Tax(taxID),          
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID) 
);
GO

-- Tạo bảng HistoryCheckin
CREATE TABLE HistoryCheckin (
    historyCheckInID NVARCHAR(15) NOT NULL PRIMARY KEY,        
    checkInDate DATETIME NOT NULL,                            
    reservationFormID NVARCHAR(15) NOT NULL,                       
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  
);
GO

-- Tạo bảng HistoryCheckOut
CREATE TABLE HistoryCheckOut (
    historyCheckOutID NVARCHAR(15) NOT NULL PRIMARY KEY,      
    checkOutDate DATETIME NOT NULL,                         
    reservationFormID NVARCHAR(15) NOT NULL,                
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID) 
);
GO

-- Tạo bảng RoomReservationDetail
CREATE TABLE RoomReservationDetail (
    roomReservationDetailID NVARCHAR(15) NOT NULL PRIMARY KEY,   
    dateChanged DATETIME NOT NULL,  
    roomID NVARCHAR(15) NOT NULL,    
    reservationFormID NVARCHAR(15) NOT NULL,  
    FOREIGN KEY (roomID) REFERENCES Room(roomID),  
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  
);
GO

-- ===================================================================================
-- 2. THÊM DỮ LIỆU
-- ===================================================================================

-- Thêm dữ liệu vào bảng GlobalSequence
INSERT INTO GlobalSequence(tableName, nextID)
VALUES
    ('Employee', 'EMP-000006'),
    ('Account', 'ACC-000006'),
	('ServiceCategory', 'SC-000005'),
	('HotelService', 'HS-000008'),
	('Pricing', 'P-000009'),
	('RoomCategory', 'RC-000005'),
    ('ShiftAssignment', 'SA-000004')
GO

-- Thêm dữ liệu vào bảng Employee
INSERT INTO Employee (employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position)
VALUES 
    ('EMP-000001', N'Huynh Duc Phu', '0912345678', 'phuhuynh@gmail.com', N'123 Ho Chi Minh', 'MALE', '001099012345', '1985-06-15', 'MANAGER'),
    ('EMP-000002', N'Nguyen Xuan Chuc', '0908765432', 'chucnguyen@yahoo.com', N'456 Hue', 'MALE', '002199012346', '1990-04-22', 'RECEPTIONIST'),
    ('EMP-000003', N'Le Tran Gia Huy', '0987654321', 'huytranle@gmail.com', N'789 Ho Chi Minh', 'MALE', '003299012347', '1992-08-19', 'MANAGER'),
    ('EMP-000004', N'Dang Nguyen Tien Phat', '0934567890', 'phatnguyen@gmail.com', N'321 Binh Phuoc', 'MALE', '004399012348', '1987-12-05', 'RECEPTIONIST'),
    ('EMP-000005', N'Vu Ba Hai', '0923456789', 'vubachai@yahoo.com', N'654 Long An', 'MALE', '004399012349', '1995-03-30', 'MANAGER');
GO

-- Thêm dữ liệu vào bảng Account
INSERT INTO Account (accountID, userName, password, status, employeeID)
VALUES 
    ('ACC-000001', N'huynhducphu', N'test123@', N'ACTIVE', 'EMP-000001'),
    ('ACC-000002', N'nguyenxuanchuc', N'test123@', N'ACTIVE', 'EMP-000002'),
    ('ACC-000003', N'letranle', N'test123@', N'LOCKED', 'EMP-000003'),
    ('ACC-000004', N'tienphat', N'test123@', N'INACTIVE', 'EMP-000004'),
    ('ACC-000005', N'vubahai', N'test123@', N'ACTIVE', 'EMP-000005');
GO

-- Thêm dữ liệu vào bảng ServiceCategory
INSERT INTO ServiceCategory (serviceCategoryID, serviceCategoryName)
VALUES
    ('SC-000001', N'Giải trí'),  
    ('SC-000002', N'Ăn uống'),        
    ('SC-000003', N'Chăm sóc và sức khỏe'),
    ('SC-000004', N'Vận chuyển');
GO

-- Thêm dữ liệu vào bảng HotelService
INSERT INTO HotelService (hotelServiceId, serviceName, description, servicePrice, serviceCategoryID)
VALUES
    ('HS-000001', N'Dịch vụ Karaoke', N'Phòng karaoke chất lượng cao, tiêu chuẩn giải trí gia đình', 100.00, 'SC-000001'),
    ('HS-000002', N'Hồ bơi', N'Sử dụng hồ bơi ngoài trời cho khách nghỉ', 50.00, 'SC-000001'),
    ('HS-000003', N'Bữa sáng tự chọn', N'Bữa sáng buffet với đa dạng món ăn', 30.00, 'SC-000002'),
    ('HS-000004', N'Thức uống tại phòng', N'Phục vụ thức uống tại phòng', 20.00, 'SC-000002'),
    ('HS-000005', N'Dịch vụ Spa', N'Massage toàn thân và liệu trình chăm sóc da', 120.00, 'SC-000003'),
    ('HS-000006', N'Chăm sóc trẻ em', N'Chăm sóc trẻ dưới 10 tuổi', 80.00, 'SC-000003'),
    ('HS-000007', N'Thuê xe', N'Thuê xe di chuyển trong thành phố', 150.00, 'SC-000004');
GO

-- Thêm dữ liệu vào bảng RoomCategory
INSERT INTO RoomCategory (roomCategoryID, roomCategoryName, numberOfBed)
VALUES 
    ('RC-000001', N'Phòng Thường Giường Đơn', 1),
    ('RC-000002', N'Phòng Thường Giường Đôi', 2),
    ('RC-000003', N'Phòng VIP Giường Đơn', 1),
    ('RC-000004', N'Phòng VIP Giường Đôi', 2);
GO

-- Thêm dữ liệu vào bảng Pricing
INSERT INTO Pricing (pricingID, priceUnit, price, roomCategoryID)
VALUES 
    ('P-000001', N'HOUR', 150000.00, 'RC-000001'),
    ('P-000002', N'DAY', 800000.00, 'RC-000001'),
    ('P-000003', N'HOUR', 200000.00, 'RC-000002'),
    ('P-000004', N'DAY', 850000.00, 'RC-000002'),
    ('P-000005', N'HOUR', 300000.00, 'RC-000003'),
    ('P-000006', N'DAY', 1600000.00, 'RC-000003'),
    ('P-000007', N'HOUR', 400000.00, 'RC-000004'),
    ('P-000008', N'DAY', 1800000.00, 'RC-000004');
GO

-- Thêm dữ liệu vào bảng Room với mã phòng mới
INSERT INTO Room (roomID, roomStatus, dateOfCreation, roomCategoryID)
VALUES 
    ('T1101', N'ON_USE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2102', N'ON_USE', '2024-09-28 10:00:00', 'RC-000002'),
    ('T2203', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
    ('V2304', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T2105', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2206', N'ON_USE', '2024-09-28 10:00:00', 'RC-000002'),
    ('T2307', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
    ('V2408', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1109', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V1210', N'ON_USE', '2024-09-28 10:00:00', 'RC-000002');
GO


-- Thêm dữ liệu vào bảng Shift
INSERT INTO Shift (shiftID, startTime, endTime, modifiedDate, numberOfHour, shiftDaysSchedule)
VALUES
    ('SHIFT-AM-0001', '05:00', '11:00', GETDATE(), 6, N'MON_WEB_FRI'),
    ('SHIFT-PM-0002', '11:00', '17:00', GETDATE(), 6, N'MON_WEB_FRI'),
    ('SHIFT-PM-0003', '17:00', '23:00', GETDATE(), 6, N'TUE_THU_SAT'),
    ('SHIFT-AM-0004', '05:00', '11:00', GETDATE(), 6, N'TUE_THU_SAT'),
    ('SHIFT-PM-0005', '11:00', '17:00', GETDATE(), 6, N'SUNDAY'),
    ('SHIFT-PM-0006', '17:00', '23:00', GETDATE(), 6, N'SUNDAY');
GO

-- Thêm dữ liệu vào bảng ShiftAssignment
INSERT INTO ShiftAssignment (shiftAssignmentId, description, shiftId, employeeId)
VALUES
    ('SA-000001', N'Assigned to morning shift', 'SHIFT-AM-0001', 'EMP-000001'),
    ('SA-000002', N'Assigned to evening shift', 'SHIFT-PM-0002', 'EMP-000002'),
    ('SA-000003', N'Assigned to night shift', 'SHIFT-PM-0003', 'EMP-000003'),
    ('SA-000004', N'Assigned to morning shift', 'SHIFT-AM-0004', 'EMP-000004');
GO

-- Thêm dữ liệu vào bảng Customer
INSERT INTO Customer (customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob)
VALUES
    ('CUS-000001', N'Nguyen Van A', '0912345678', 'nguyenvana@gmail.com', N'123 Duong ABC, Quan 1, TP HCM', N'MALE', '001099012333', '1990-05-15'),
    ('CUS-000002', N'Le Thi B', '0912345679', 'lethib@gmail.com', N'456 Duong XYZ, Quan 3, TP HCM', N'FEMALE', '001099012323', '1992-07-22'),
    ('CUS-000003', N'Tran Van C', '0912345680', 'tranvanc@gmail.com', N'789 Duong MNO, Quan 5, TP HCM', N'MALE', '001099012343', '1988-03-30'),
    ('CUS-000004', N'Pham Thi D', '0912345681', 'phamthid@gmail.com', N'321 Duong PQR, Quan 7, TP HCM', N'FEMALE', '001099012546', '1995-12-01'),
    ('CUS-000005', N'Hoang Van E', '0912345682', 'hoangvane@gmail.com', N'987 Duong STU, Quan 10, TP HCM', N'MALE', '001099012764', '1991-11-20');
GO

-- ===================================================================================
-- 3. TRIGGER 
-- ===================================================================================
-- Tạo trigger để giới hạn mỗi roomCategoryID chỉ có 2 bản ghi trong bảng Pricing
CREATE TRIGGER trg_LimitPricingForRoomCategory
ON Pricing
AFTER INSERT, UPDATE
AS
BEGIN
    IF EXISTS (
        SELECT roomCategoryID
        FROM Pricing
        GROUP BY roomCategoryID
        HAVING COUNT(*) > 2
    )
    BEGIN
        RAISERROR('Mỗi loại phòng chỉ được phép có 2 bản ghi trong Pricing (1 DAY và 1 HOUR)', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO


