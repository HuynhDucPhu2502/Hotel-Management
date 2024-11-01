-- Tạo cơ sở dữ liệu HotelDatabase
USE master;
GO
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
    serviceCategoryName NVARCHAR(50) NOT NULL,
	icon NVARCHAR(255)
);
GO

-- Tạo bảng HotelService
CREATE TABLE HotelService (
    hotelServiceId NVARCHAR(15) NOT NULL PRIMARY KEY,
    serviceName NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NOT NULL,
    servicePrice MONEY NOT NULL,
    serviceCategoryID NVARCHAR(15) NULL,

    CONSTRAINT FK_HotelService_ServiceCategory
        FOREIGN KEY (serviceCategoryID)
        REFERENCES ServiceCategory(serviceCategoryID)
        ON DELETE SET NULL
		ON UPDATE CASCADE
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
    roomStatus NVARCHAR(20) NOT NULL CHECK (roomStatus IN ('AVAILABLE', 'ON_USE', 'UNAVAILABLE', 'OVERDUE')),
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
    numberOfHour INT NOT NULL,
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
	roomBookingDeposit FLOAT NOT NULL,
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID) ON DELETE SET NULL,
    FOREIGN KEY (roomID) REFERENCES Room(roomID) ON DELETE SET NULL,
    FOREIGN KEY (customerID) REFERENCES Customer(customerID) ON DELETE SET NULL
);
GO

-- Tạo bảng RoomUsageService
CREATE TABLE RoomUsageService (
    roomUsageServiceId NVARCHAR(15) NOT NULL PRIMARY KEY,
    quantity INT NOT NULL,
    unitPrice DECIMAL(18, 2) NOT NULL,
    totalPrice AS (quantity * unitPrice) PERSISTED,
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
    employeeID NVARCHAR(15),  -- Thêm employeeID
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- Tạo bảng HistoryCheckOut
CREATE TABLE HistoryCheckOut (
    historyCheckOutID NVARCHAR(15) NOT NULL PRIMARY KEY,
    checkOutDate DATETIME NOT NULL,
    reservationFormID NVARCHAR(15) NOT NULL,
    employeeID NVARCHAR(15),  -- Thêm employeeID
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- Tạo bảng RoomReservationDetail
CREATE TABLE RoomReservationDetail (
    roomReservationDetailID NVARCHAR(15) NOT NULL PRIMARY KEY,
    dateChanged DATETIME NOT NULL,
    roomID NVARCHAR(15) NOT NULL,
    reservationFormID NVARCHAR(15) NOT NULL,
    employeeID NVARCHAR(15),
    FOREIGN KEY (roomID) REFERENCES Room(roomID),
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
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
	('ShiftAssignment', 'SA-000004'),
	('Customer', 'CUS-000031'),
	('ReservationForm', 'RF-000110'),
	('HistoryCheckIn', 'HCI-000003'),
	('RoomReservationDetail', 'RRD-000003'),
	('RoomUsageService', 'RUS-000001');
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

-- Thêm dữ liệu vào bảng ServiceCategory với icon tương ứng
INSERT INTO ServiceCategory (serviceCategoryID, serviceCategoryName, icon)
VALUES
    ('SC-000001', N'Giải trí', 'karaoke'),
    ('SC-000002', N'Ăn uống', 'food'),
    ('SC-000003', N'Chăm sóc và sức khỏe', 'massage'),
    ('SC-000004', N'Vận chuyển', 'car');
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
    ('HS-000007', N'Thuê xe', N'Thuê xe di chuyển trong thành phố', 150.00, 'SC-000004'),
    ('HS-000008', N'Dịch vụ Xông hơi', N'Xông hơi thư giãn cơ thể và tâm trí', 900000, 'SC-000001'),
    ('HS-000009', N'Phòng Gym', N'Trung tâm thể hình với trang thiết bị hiện đại', 700000, 'SC-000001'),
    ('HS-000010', N'Trò chơi điện tử', N'Khu vực giải trí với các trò chơi điện tử', 500000, 'SC-000001'),
    ('HS-000011', N'Buffet tối', N'Thực đơn buffet với đa dạng món ăn', 2000000, 'SC-000002'),
    ('HS-000012', N'Quầy bar', N'Thưởng thức cocktail và các loại rượu tại quầy', 800000, 'SC-000002'),
    ('HS-000013', N'Dịch vụ Cà phê', N'Cà phê và đồ uống nóng phục vụ cả ngày', 300000, 'SC-000002'),
    ('HS-000014', N'Dịch vụ Tóc', N'Tạo kiểu và chăm sóc tóc chuyên nghiệp', 600000, 'SC-000003'),
    ('HS-000015', N'Tắm trắng', N'Liệu trình tắm trắng da toàn thân', 1500000, 'SC-000003'),
    ('HS-000016', N'Yoga & Thiền', N'Lớp yoga và thiền hàng ngày', 1000000, 'SC-000003'),
    ('HS-000017', N'Xe đưa đón sân bay', N'Dịch vụ đưa đón từ sân bay về khách sạn', 1200000, 'SC-000004'),
    ('HS-000018', N'Thuê xe đạp', N'Thuê xe đạp tham quan quanh thành phố', 400000, 'SC-000004'),
    ('HS-000019', N'Thuê xe điện', N'Thuê xe điện cho các chuyến đi ngắn', 600000, 'SC-000004'),
    ('HS-000020', N'Dịch vụ Thư ký', N'Hỗ trợ thư ký và in ấn tài liệu', 1000000, 'SC-000004');
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
    ('T1101', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2102', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
    ('T1203', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
    ('V2304', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1105', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2206', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
    ('T1307', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
    ('V2408', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1109', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2210', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002');
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
    ('CUS-000005', N'Hoang Van E', '0912345682', 'hoangvane@gmail.com', N'987 Duong STU, Quan 10, TP HCM', N'MALE', '001099012764', '1991-11-20'),
	('CUS-000006', N'Nguyen Van F', '0912345683', 'nguyenf@gmail.com', N'111 Duong DEF, Quan 1, TP HCM', N'MALE', '001099012765', '1989-04-18'),
    ('CUS-000007', N'Le Thi G', '0912345684', 'lethig@gmail.com', N'222 Duong GHI, Quan 2, TP HCM', N'FEMALE', '001099012776', '1993-08-29'),
    ('CUS-000008', N'Tran Van H', '0912345685', 'tranvanh@gmail.com', N'333 Duong JKL, Quan 3, TP HCM', N'MALE', '001099012787', '1985-12-12'),
    ('CUS-000009', N'Pham Thi I', '0912345686', 'phamthi@gmail.com', N'444 Duong MNO, Quan 4, TP HCM', N'FEMALE', '001099012798', '1990-01-01'),
    ('CUS-000010', N'Hoang Van J', '0912345687', 'hoangvanj@gmail.com', N'555 Duong PQR, Quan 5, TP HCM', N'MALE', '001099012809', '1987-05-05'),
    ('CUS-000011', N'Nguyen Van K', '0912345688', 'nguyenk@gmail.com', N'666 Duong STU, Quan 6, TP HCM', N'MALE', '001099012810', '1992-11-11'),
    ('CUS-000012', N'Le Thi L', '0912345689', 'lethil@gmail.com', N'777 Duong VWX, Quan 7, TP HCM', N'FEMALE', '001099012821', '1994-02-15'),
    ('CUS-000013', N'Tran Van M', '0912345690', 'tranvanm@gmail.com', N'888 Duong YZA, Quan 8, TP HCM', N'MALE', '001099012832', '1986-09-09'),
    ('CUS-000014', N'Pham Thi N', '0912345691', 'phamthinh@gmail.com', N'999 Duong BCD, Quan 9, TP HCM', N'FEMALE', '001099012843', '1991-03-03'),
    ('CUS-000015', N'Hoang Van O', '0912345692', 'hoangvano@gmail.com', N'000 Duong EFG, Quan 10, TP HCM', N'MALE', '001099012854', '1993-07-21'),
    ('CUS-000016', N'Nguyen Van P', '0912345693', 'nguyenp@gmail.com', N'123 Duong HIJ, Quan 11, TP HCM', N'MALE', '001099012865', '1990-04-04'),
    ('CUS-000017', N'Le Thi Q', '0912345694', 'lethiq@gmail.com', N'234 Duong KLM, Quan 12, TP HCM', N'FEMALE', '001099012876', '1988-06-06'),
    ('CUS-000018', N'Tran Van R', '0912345695', 'tranvanr@gmail.com', N'345 Duong NOP, Quan 1, TP HCM', N'MALE', '001099012887', '1995-08-08'),
    ('CUS-000019', N'Pham Thi S', '0912345696', 'phamthir@gmail.com', N'456 Duong QRS, Quan 2, TP HCM', N'FEMALE', '001099012898', '1994-05-05'),
    ('CUS-000020', N'Hoang Van T', '0912345697', 'hoangvant@gmail.com', N'567 Duong TUV, Quan 3, TP HCM', N'MALE', '001099012909', '1990-02-02'),
    ('CUS-000021', N'Nguyen Van U', '0912345698', 'nguyenu@gmail.com', N'678 Duong WXY, Quan 4, TP HCM', N'MALE', '001099012910', '1989-11-11'),
    ('CUS-000022', N'Le Thi V', '0912345699', 'lethiv@gmail.com', N'789 Duong ZAB, Quan 5, TP HCM', N'FEMALE', '001099012921', '1992-09-09'),
    ('CUS-000023', N'Tran Van W', '0912345700', 'tranvanw@gmail.com', N'890 Duong CDE, Quan 6, TP HCM', N'MALE', '001099012932', '1993-10-10'),
    ('CUS-000024', N'Pham Thi X', '0912345701', 'phamthix@gmail.com', N'901 Duong FGHI, Quan 7, TP HCM', N'FEMALE', '001099012943', '1987-12-12'),
	('CUS-000025', N'Hoang Van Y', '0912345702', 'hoangvany@gmail.com', N'012 Duong JKL, Quan 8, TP HCM', N'MALE', '001099012954', '1988-01-01'),
    ('CUS-000026', N'Nguyen Van Z', '0912345703', 'nguyenz@gmail.com', N'123 Duong MNO, Quan 9, TP HCM', N'MALE', '001099012965', '1991-04-04'),
    ('CUS-000027', N'Le Thi AA', '0912345704', 'lethia@gmail.com', N'234 Duong PQR, Quan 10, TP HCM', N'FEMALE', '001099012976', '1990-12-12'),
    ('CUS-000028', N'Tran Van AB', '0912345705', 'tranvanab@gmail.com', N'345 Duong STU, Quan 1, TP HCM', N'MALE', '001099012987', '1986-07-07'),
    ('CUS-000029', N'Pham Thi AC', '0912345706', 'phamthiac@gmail.com', N'456 Duong VWX, Quan 2, TP HCM', N'FEMALE', '001099012998', '1994-03-03'),
    ('CUS-000030', N'Hoang Van AD', '0912345707', 'hoangvanad@gmail.com', N'567 Duong YZA, Quan 3, TP HCM', N'MALE', '001099013000', '1992-06-06');
GO

-- Thêm dữ liệu vào bảng ReservationForm
INSERT INTO ReservationForm(reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit)
VALUES
	('RF-000001', '2021-01-05', '2021-01-06', '2021-01-08', 'EMP-000001', 'T1101', 'CUS-000005', 480000),
	('RF-000002', '2021-01-10', '2021-01-11', '2021-01-13', 'EMP-000002', 'T1109', 'CUS-000002', 480000),
	('RF-000003', '2021-02-15', '2021-02-16', '2021-02-19', 'EMP-000003', 'T1105', 'CUS-000010', 720000),
	('RF-000004', '2021-03-20', '2021-03-21', '2021-03-24', 'EMP-000004', 'T1203', 'CUS-000015', 720000),
	('RF-000005', '2021-04-10', '2021-04-11', '2021-04-14', 'EMP-000005', 'T1307', 'CUS-000007', 1440000),
    ('RF-000006', '2021-05-22', '2021-05-23', '2021-05-25', 'EMP-000001', 'V2210', 'CUS-000011', 1440000),
    ('RF-000007', '2021-06-15', '2021-06-16', '2021-06-18', 'EMP-000002', 'V2102', 'CUS-000001', 510000),
    ('RF-000008', '2021-07-05', '2021-07-06', '2021-07-08', 'EMP-000003', 'V2206', 'CUS-000016', 510000),
    ('RF-000009', '2021-08-11', '2021-08-12', '2021-08-14', 'EMP-000004', 'V2304', 'CUS-000009', 1080000),
    ('RF-000010', '2021-09-25', '2021-09-26', '2021-09-28', 'EMP-000005', 'V2408', 'CUS-000014', 1080000),
    ('RF-000011', '2021-10-10', '2021-10-11', '2021-10-13', 'EMP-000001', 'T1101', 'CUS-000013', 480000),
    ('RF-000012', '2021-11-20', '2021-11-21', '2021-11-23', 'EMP-000002', 'T1109', 'CUS-000008', 480000),
    ('RF-000013', '2021-12-02', '2021-12-03', '2021-12-05', 'EMP-000003', 'T1105', 'CUS-000006', 720000),
    ('RF-000014', '2021-12-15', '2021-12-16', '2021-12-18', 'EMP-000004', 'T1203', 'CUS-000017', 720000),
    ('RF-000015', '2021-12-28', '2021-12-29', '2021-12-31', 'EMP-000005', 'T1307', 'CUS-000004', 1440000),
    ('RF-000016', '2022-01-05', '2022-01-06', '2022-01-08', 'EMP-000001', 'V2210', 'CUS-000019', 1440000),
    ('RF-000017', '2022-01-15', '2022-01-16', '2022-01-18', 'EMP-000002', 'V2102', 'CUS-000003', 510000),
    ('RF-000018', '2022-02-10', '2022-02-11', '2022-02-13', 'EMP-000003', 'V2206', 'CUS-000018', 510000),
    ('RF-000019', '2022-02-20', '2022-02-21', '2022-02-23', 'EMP-000004', 'V2304', 'CUS-000021', 1080000),
    ('RF-000020', '2022-03-10', '2022-03-11', '2022-03-14', 'EMP-000005', 'V2408', 'CUS-000020', 1080000),
	('RF-000021', '2022-04-05', '2022-04-06', '2022-04-09', 'EMP-000001', 'T1101', 'CUS-000017', 480000),
    ('RF-000022', '2022-04-20', '2022-04-21', '2022-04-24', 'EMP-000002', 'T1109', 'CUS-000023', 480000),
    ('RF-000023', '2022-05-15', '2022-05-16', '2022-05-19', 'EMP-000003', 'T1105', 'CUS-000012', 720000),
    ('RF-000024', '2022-06-10', '2022-06-11', '2022-06-13', 'EMP-000004', 'T1203', 'CUS-000009', 720000),
    ('RF-000025', '2022-06-25', '2022-06-26', '2022-06-29', 'EMP-000005', 'T1307', 'CUS-000026', 1440000),
	('RF-000026', '2022-07-05', '2022-07-06', '2022-07-09', 'EMP-000001', 'V2210', 'CUS-000027', 1440000),
    ('RF-000027', '2022-07-20', '2022-07-21', '2022-07-24', 'EMP-000002', 'V2102', 'CUS-000022', 510000),
    ('RF-000028', '2022-08-05', '2022-08-06', '2022-08-08', 'EMP-000003', 'V2206', 'CUS-000028', 510000),
    ('RF-000029', '2022-08-20', '2022-08-21', '2022-08-23', 'EMP-000004', 'V2304', 'CUS-000010', 1080000),
    ('RF-000030', '2022-09-15', '2022-09-16', '2022-09-18', 'EMP-000005', 'V2408', 'CUS-000005', 1080000),
	('RF-000031', '2022-10-10', '2022-10-11', '2022-10-13', 'EMP-000001', 'T1101', 'CUS-000019', 480000),
    ('RF-000032', '2022-10-20', '2022-10-21', '2022-10-23', 'EMP-000002', 'T1109', 'CUS-000011', 480000),
    ('RF-000033', '2022-11-05', '2022-11-06', '2022-11-09', 'EMP-000003', 'T1105', 'CUS-000015', 720000),
    ('RF-000034', '2022-11-25', '2022-11-26', '2022-11-29', 'EMP-000004', 'T1203', 'CUS-000006', 720000),
    ('RF-000035', '2022-12-01', '2022-12-02', '2022-12-05', 'EMP-000005', 'T1307', 'CUS-000012', 1440000),
    ('RF-000036', '2022-12-05', '2022-12-06', '2022-12-08', 'EMP-000001', 'V2210', 'CUS-000009', 1440000),
    ('RF-000037', '2022-12-10', '2022-12-11', '2022-12-13', 'EMP-000002', 'V2102', 'CUS-000025', 510000),
    ('RF-000038', '2022-12-15', '2022-12-16', '2022-12-18', 'EMP-000003', 'V2206', 'CUS-000017', 510000),
    ('RF-000039', '2022-12-20', '2022-12-21', '2022-12-24', 'EMP-000004', 'V2304', 'CUS-000014', 1080000),
    ('RF-000040', '2022-12-25', '2022-12-26', '2022-12-28', 'EMP-000005', 'V2408', 'CUS-000030', 1080000),
	('RF-000041', '2023-01-15', '2023-01-16', '2023-01-18', 'EMP-000001', 'T1101', 'CUS-000022', 480000),
    ('RF-000042', '2023-01-22', '2023-01-23', '2023-01-25', 'EMP-000002', 'T1109', 'CUS-000006', 480000),
    ('RF-000043', '2023-02-10', '2023-02-11', '2023-02-13', 'EMP-000003', 'T1105', 'CUS-000028', 720000),
    ('RF-000044', '2023-02-20', '2023-02-21', '2023-02-23', 'EMP-000004', 'T1203', 'CUS-000011', 720000),
    ('RF-000045', '2023-03-05', '2023-03-06', '2023-03-08', 'EMP-000005', 'T1307', 'CUS-000004', 1440000),
    ('RF-000046', '2023-03-15', '2023-03-16', '2023-03-18', 'EMP-000001', 'V2210', 'CUS-000019', 1440000),
    ('RF-000047', '2023-04-01', '2023-04-02', '2023-04-04', 'EMP-000002', 'V2102', 'CUS-000008', 510000),
    ('RF-000048', '2023-04-12', '2023-04-13', '2023-04-15', 'EMP-000003', 'V2206', 'CUS-000013', 510000),
    ('RF-000049', '2023-05-05', '2023-05-06', '2023-05-08', 'EMP-000004', 'V2304', 'CUS-000021', 1080000),
    ('RF-000050', '2023-05-20', '2023-05-21', '2023-05-23', 'EMP-000005', 'V2408', 'CUS-000007', 1080000),
	('RF-000051', '2023-06-05', '2023-06-06', '2023-06-09', 'EMP-000001', 'T1101', 'CUS-000001', 480000),
    ('RF-000052', '2023-06-15', '2023-06-16', '2023-06-18', 'EMP-000002', 'T1109', 'CUS-000015', 480000),
    ('RF-000053', '2023-07-01', '2023-07-02', '2023-07-04', 'EMP-000003', 'T1105', 'CUS-000012', 720000),
    ('RF-000054', '2023-07-20', '2023-07-21', '2023-07-23', 'EMP-000004', 'T1203', 'CUS-000026', 720000),
    ('RF-000055', '2023-08-05', '2023-08-06', '2023-08-08', 'EMP-000005', 'T1307', 'CUS-000023', 1440000),
    ('RF-000056', '2023-08-15', '2023-08-16', '2023-08-18', 'EMP-000001', 'V2210', 'CUS-000010', 1440000),
    ('RF-000057', '2023-09-10', '2023-09-11', '2023-09-13', 'EMP-000002', 'V2102', 'CUS-000005', 510000),
    ('RF-000058', '2023-09-20', '2023-09-21', '2023-09-24', 'EMP-000003', 'V2206', 'CUS-000020', 510000),
    ('RF-000059', '2023-10-01', '2023-10-02', '2023-10-04', 'EMP-000004', 'V2304', 'CUS-000003', 1080000),
    ('RF-000060', '2023-10-15', '2023-10-16', '2023-10-19', 'EMP-000005', 'V2408', 'CUS-000027', 1080000),
	('RF-000061', '2023-11-05', '2023-11-06', '2023-11-08', 'EMP-000001', 'T1101', 'CUS-000016', 480000),
    ('RF-000062', '2023-11-15', '2023-11-16', '2023-11-18', 'EMP-000002', 'T1109', 'CUS-000024', 480000),
    ('RF-000063', '2023-11-22', '2023-11-23', '2023-11-25', 'EMP-000003', 'T1105', 'CUS-000018', 720000),
    ('RF-000064', '2023-12-01', '2023-12-02', '2023-12-04', 'EMP-000004', 'T1203', 'CUS-000029', 720000),
    ('RF-000065', '2023-12-05', '2023-12-06', '2023-12-09', 'EMP-000005', 'T1307', 'CUS-000002', 1440000),
    ('RF-000066', '2023-12-10', '2023-12-11', '2023-12-13', 'EMP-000001', 'V2210', 'CUS-000009', 1440000),
    ('RF-000067', '2023-12-15', '2023-12-16', '2023-12-18', 'EMP-000002', 'V2102', 'CUS-000011', 510000),
    ('RF-000068', '2023-12-20', '2023-12-21', '2023-12-23', 'EMP-000003', 'V2206', 'CUS-000020', 510000),
    ('RF-000069', '2023-12-25', '2023-12-26', '2023-12-28', 'EMP-000004', 'V2304', 'CUS-000004', 1080000),
    ('RF-000070', '2023-12-28', '2023-12-29', '2023-12-31', 'EMP-000005', 'V2408', 'CUS-000030', 1080000),
	('RF-000071', '2024-01-05', '2024-01-06', '2024-01-08', 'EMP-000001', 'T1101', 'CUS-000007', 240000),
    ('RF-000072', '2024-01-10', '2024-01-11', '2024-01-14', 'EMP-000002', 'T1109', 'CUS-000019', 240000),
    ('RF-000073', '2024-01-20', '2024-01-21', '2024-01-24', 'EMP-000003', 'T1105', 'CUS-000013', 255000),
    ('RF-000074', '2024-02-02', '2024-02-03', '2024-02-06', 'EMP-000004', 'T1203', 'CUS-000025', 255000),
    ('RF-000075', '2024-02-15', '2024-02-16', '2024-02-18', 'EMP-000005', 'T1307', 'CUS-000002', 480000),
    ('RF-000076', '2024-02-25', '2024-02-26', '2024-02-28', 'EMP-000001', 'V2210', 'CUS-000016', 480000),
    ('RF-000077', '2024-03-03', '2024-03-04', '2024-03-06', 'EMP-000002', 'V2102', 'CUS-000011', 255000),
    ('RF-000078', '2024-03-15', '2024-03-16', '2024-03-19', 'EMP-000003', 'V2206', 'CUS-000021', 540000),
    ('RF-000079', '2024-03-25', '2024-03-26', '2024-03-29', 'EMP-000004', 'V2304', 'CUS-000027', 540000),
    ('RF-000080', '2024-04-05', '2024-04-06', '2024-04-09', 'EMP-000005', 'V2408', 'CUS-000005', 540000),
	('RF-000081', '2024-04-15', '2024-04-16', '2024-04-19', 'EMP-000001', 'T1101', 'CUS-000028', 240000),
    ('RF-000082', '2024-05-01', '2024-05-02', '2024-05-04', 'EMP-000002', 'T1109', 'CUS-000004', 240000),
    ('RF-000083', '2024-05-10', '2024-05-11', '2024-05-14', 'EMP-000003', 'T1105', 'CUS-000009', 255000),
    ('RF-000084', '2024-05-20', '2024-05-21', '2024-05-24', 'EMP-000004', 'T1203', 'CUS-000012', 255000),
    ('RF-000085', '2024-05-25', '2024-05-26', '2024-05-29', 'EMP-000005', 'T1307', 'CUS-000020', 480000),
    ('RF-000086', '2024-06-01', '2024-06-02', '2024-06-04', 'EMP-000001', 'V2210', 'CUS-000018', 480000),
    ('RF-000087', '2024-06-10', '2024-06-11', '2024-06-14', 'EMP-000002', 'V2102', 'CUS-000014', 255000),
    ('RF-000088', '2024-06-15', '2024-06-16', '2024-06-19', 'EMP-000003', 'V2206', 'CUS-000017', 540000),
    ('RF-000089', '2024-06-25', '2024-06-26', '2024-06-28', 'EMP-000004', 'V2304', 'CUS-000003', 540000),
    ('RF-000090', '2024-07-01', '2024-07-02', '2024-07-04', 'EMP-000005', 'V2408', 'CUS-000006', 540000),
	('RF-000091', '2024-07-05', '2024-07-06', '2024-07-09', 'EMP-000001', 'T1101', 'CUS-000029', 240000),
    ('RF-000092', '2024-07-10', '2024-07-11', '2024-07-14', 'EMP-000002', 'T1109', 'CUS-000022', 240000),
    ('RF-000093', '2024-07-15', '2024-07-16', '2024-07-19', 'EMP-000003', 'T1105', 'CUS-000010', 255000),
    ('RF-000094', '2024-07-20', '2024-07-21', '2024-07-24', 'EMP-000004', 'T1203', 'CUS-000008', 255000),
    ('RF-000095', '2024-07-25', '2024-07-26', '2024-07-29', 'EMP-000005', 'T1307', 'CUS-000026', 480000),
    ('RF-000096', '2024-08-01', '2024-08-02', '2024-08-04', 'EMP-000001', 'V2210', 'CUS-000015', 480000),
    ('RF-000097', '2024-08-10', '2024-08-11', '2024-08-14', 'EMP-000002', 'V2102', 'CUS-000001', 255000),
    ('RF-000098', '2024-08-15', '2024-08-16', '2024-08-19', 'EMP-000003', 'V2206', 'CUS-000024', 540000),
    ('RF-000099', '2024-08-25', '2024-08-26', '2024-08-28', 'EMP-000004', 'V2304', 'CUS-000023', 540000),
    ('RF-000100', '2024-09-01', '2024-09-02', '2024-09-04', 'EMP-000005', 'V2408', 'CUS-000006', 540000),
    ('RF-000101', '2024-09-05', '2024-09-06', '2024-09-09', 'EMP-000001', 'T1101', 'CUS-000030', 240000),
    ('RF-000102', '2024-09-10', '2024-09-11', '2024-09-14', 'EMP-000002', 'T1109', 'CUS-000025', 240000),
    ('RF-000103', '2024-09-15', '2024-09-16', '2024-09-19', 'EMP-000003', 'T1105', 'CUS-000023', 255000),
    ('RF-000104', '2024-09-20', '2024-09-21', '2024-09-24', 'EMP-000004', 'T1203', 'CUS-000022', 255000),
    ('RF-000105', '2024-09-25', '2024-09-26', '2024-09-28', 'EMP-000005', 'T1307', 'CUS-000021', 480000),
    ('RF-000106', '2024-10-01', '2024-10-02', '2024-10-04', 'EMP-000001', 'V2210', 'CUS-000023', 480000);


-- Thêm dữ liệu vào bảng Tax
INSERT INTO Tax (taxID, taxName, taxRate, dateOfCreation, activate)
VALUES
    ('tax-000001', 'tax1', 0.1, '2024-09-29', 1),
    ('tax-000002', 'tax2', 0.15, '2024-09-29', 1),
    ('tax-000003', 'tax3', 0.08, '2024-10-01', 1),
    ('tax-000004', 'tax4', 0.12, '2024-10-02', 0),
    ('tax-000005', 'tax5', 0.07, '2024-10-03', 1),
    ('tax-000006', 'tax6', 0.2,  '2024-10-04', 1),
    ('tax-000007', 'tax7', 0.05, '2024-10-05', 0),
    ('tax-000008', 'tax8', 0.1,  '2024-10-06', 1),
    ('tax-000009', 'tax9', 0.18, '2024-10-07', 0),
    ('tax-000010', 'tax10', 0.09, '2024-10-08', 1),
    ('tax-000011', 'tax11', 0.14, '2024-10-09', 1);
GO

-- Thêm dữ liệu vào bảng Invoice
INSERT INTO Invoice(invoiceID, invoiceDate, roomCharge, servicesCharge, totalDue, netDue, taxID, reservationFormID)
VALUES
    ('INV-0000000001', '2021-01-08', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000001'),
    ('INV-0000000002', '2021-01-13', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000002'),
    ('INV-0000000003', '2021-02-19', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000003'),
    ('INV-0000000004', '2021-03-24', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000004'),
    ('INV-0000000005', '2021-04-14', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000005'),
    ('INV-0000000006', '2021-05-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000006'),
    ('INV-0000000007', '2021-06-18', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000007'),
    ('INV-0000000008', '2021-07-08', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000008'),
    ('INV-0000000009', '2021-08-14', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000009'),
    ('INV-0000000010', '2021-09-28', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000010'),
    ('INV-0000000011', '2021-10-13', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000011'),
    ('INV-0000000012', '2021-11-23', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000012'),
    ('INV-0000000013', '2021-12-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000013'),
    ('INV-0000000014', '2021-12-18', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000014'),
    ('INV-0000000015', '2021-12-31', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000015'),
    ('INV-0000000016', '2022-01-08', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000016'),
    ('INV-0000000017', '2022-01-18', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000017'),
    ('INV-0000000018', '2022-02-13', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000018'),
    ('INV-0000000019', '2022-02-23', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000019'),
    ('INV-0000000020', '2022-03-14', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000020'),
    ('INV-0000000021', '2022-04-09', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000021'),
    ('INV-0000000022', '2022-04-24', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000022'),
    ('INV-0000000023', '2022-05-19', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000023'),
    ('INV-0000000024', '2022-06-13', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000024'),
    ('INV-0000000025', '2022-06-29', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000025'),
    ('INV-0000000026', '2022-07-09', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000026'),
    ('INV-0000000027', '2022-07-24', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000027'),
    ('INV-0000000028', '2022-08-08', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000028'),
    ('INV-0000000029', '2022-08-23', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000029'),
    ('INV-0000000030', '2022-09-18', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000030'),
    ('INV-0000000031', '2022-10-13', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000031'),
    ('INV-0000000032', '2022-10-23', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000032'),
    ('INV-0000000033', '2022-11-09', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000033'),
    ('INV-0000000034', '2022-11-29', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000034'),
    ('INV-0000000035', '2022-12-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000035'),
    ('INV-0000000036', '2022-12-08', 12000, 6000, 18000, 16200, 'tax-000001', 'RF-000036'),
    ('INV-0000000037', '2022-12-13', 12500, 6500, 19000, 17100, 'tax-000001', 'RF-000037'),
    ('INV-0000000038', '2022-12-18', 13000, 7000, 20000, 18000, 'tax-000001', 'RF-000038'),
    ('INV-0000000039', '2022-12-24', 13500, 7200, 20700, 18630, 'tax-000001', 'RF-000039'),
    ('INV-0000000040', '2022-12-28', 14000, 7500, 21500, 19350, 'tax-000001', 'RF-000040'),
    ('INV-0000000041', '2023-01-18', 14500, 7800, 22300, 20070, 'tax-000001', 'RF-000041'),
    ('INV-0000000042', '2023-01-25', 15000, 8000, 23000, 20700, 'tax-000001', 'RF-000042'),
    ('INV-0000000043', '2023-02-13', 15500, 8300, 23800, 21420, 'tax-000001', 'RF-000043'),
    ('INV-0000000044', '2023-02-23', 16000, 8500, 24500, 22050, 'tax-000001', 'RF-000044'),
    ('INV-0000000045', '2023-03-08', 16500, 9000, 25500, 22950, 'tax-000001', 'RF-000045'),
    ('INV-0000000046', '2023-03-18', 17000, 9200, 26200, 23640, 'tax-000001', 'RF-000046'),
    ('INV-0000000047', '2023-04-04', 17500, 9500, 27000, 24300, 'tax-000001', 'RF-000047'),
    ('INV-0000000048', '2023-04-15', 18000, 9700, 27700, 24930, 'tax-000001', 'RF-000048'),
    ('INV-0000000049', '2023-05-08', 18500, 10000, 28500, 25500, 'tax-000001', 'RF-000049'),
    ('INV-0000000050', '2023-05-23', 19000, 10500, 29500, 26550, 'tax-000001', 'RF-000050'),
    ('INV-0000000051', '2023-06-09', 19500, 11000, 30500, 27450, 'tax-000001', 'RF-000051'),
    ('INV-0000000052', '2023-06-18', 20000, 11200, 31200, 28080, 'tax-000001', 'RF-000052'),
    ('INV-0000000053', '2023-07-04', 20500, 11500, 32000, 28800, 'tax-000001', 'RF-000053'),
    ('INV-0000000054', '2023-07-23', 21000, 12000, 33000, 29700, 'tax-000001', 'RF-000054'),
    ('INV-0000000055', '2023-08-08', 21500, 12500, 34000, 30600, 'tax-000001', 'RF-000055'),
    ('INV-0000000056', '2023-08-18', 22000, 13000, 35000, 31500, 'tax-000001', 'RF-000056'),
    ('INV-0000000057', '2023-09-13', 22500, 13500, 36000, 32400, 'tax-000001', 'RF-000057'),
    ('INV-0000000058', '2023-09-24', 23000, 14000, 37000, 33300, 'tax-000001', 'RF-000058'),
    ('INV-0000000059', '2023-10-04', 23500, 14500, 38000, 34200, 'tax-000001', 'RF-000059'),
    ('INV-0000000060', '2023-10-19', 24000, 15000, 39000, 35100, 'tax-000001', 'RF-000060'),
    ('INV-0000000061', '2023-11-08', 24500, 15500, 40000, 36000, 'tax-000001', 'RF-000061'),
    ('INV-0000000062', '2023-11-18', 25000, 16000, 41000, 36900, 'tax-000001', 'RF-000062'),
    ('INV-0000000063', '2023-11-25', 25500, 16500, 42000, 37800, 'tax-000001', 'RF-000063'),
    ('INV-0000000064', '2023-12-04', 26000, 17000, 43000, 38700, 'tax-000001', 'RF-000064'),
    ('INV-0000000065', '2023-12-09', 26500, 17500, 44000, 39600, 'tax-000001', 'RF-000065'),
    ('INV-0000000066', '2023-12-13', 27000, 18000, 45000, 40500, 'tax-000001', 'RF-000066'),
    ('INV-0000000067', '2023-12-18', 27500, 18500, 46000, 41400, 'tax-000001', 'RF-000067'),
    ('INV-0000000068', '2023-12-23', 28000, 19000, 47000, 42300, 'tax-000001', 'RF-000068'),
    ('INV-0000000069', '2023-12-28', 28500, 19500, 48000, 43200, 'tax-000001', 'RF-000069'),
    ('INV-0000000070', '2023-12-31', 29000, 20000, 49000, 44100, 'tax-000001', 'RF-000070'),
    ('INV-0000000071', '2024-01-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000071'),
    ('INV-0000000072', '2024-01-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000072'),
    ('INV-0000000073', '2024-01-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000073'),
    ('INV-0000000074', '2024-02-02', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000074'),
    ('INV-0000000075', '2024-02-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000075'),
    ('INV-0000000076', '2024-02-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000076'),
    ('INV-0000000077', '2024-03-03', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000077'),
    ('INV-0000000078', '2024-03-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000078'),
    ('INV-0000000079', '2024-03-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000079'),
    ('INV-0000000080', '2024-04-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000080'),
    ('INV-0000000081', '2024-04-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000081'),
    ('INV-0000000082', '2024-05-01', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000082'),
    ('INV-0000000083', '2024-05-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000083'),
    ('INV-0000000084', '2024-05-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000084'),
    ('INV-0000000085', '2024-06-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000085'),
    ('INV-0000000086', '2024-06-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000086'),
    ('INV-0000000087', '2024-06-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000087'),
    ('INV-0000000088', '2024-07-01', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000088'),
    ('INV-0000000089', '2024-07-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000089'),
    ('INV-0000000090', '2024-07-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000090'),
    ('INV-0000000091', '2024-08-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000091'),
    ('INV-0000000092', '2024-08-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000092'),
    ('INV-0000000093', '2024-08-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000093'),
    ('INV-0000000094', '2024-09-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000094'),
    ('INV-0000000095', '2024-09-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000095'),
    ('INV-0000000096', '2024-09-25', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000096'),
    ('INV-0000000097', '2024-10-01', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000097'),
    ('INV-0000000098', '2024-10-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000098'),
    ('INV-0000000099', '2024-10-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000099'),
    ('INV-0000000100', '2024-11-01', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000100'),
    ('INV-0000000101', '2024-11-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000101'),
    ('INV-0000000102', '2024-11-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000102'),
    ('INV-0000000103', '2024-12-05', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000103'),
    ('INV-0000000104', '2024-12-10', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000104'),
    ('INV-0000000105', '2024-12-15', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000105'),
    ('INV-0000000106', '2024-12-20', 10000, 5000, 15000, 13500, 'tax-000001', 'RF-000106');
GO

--Phiếu 1: đã đặt phòng, có thể checkin nhưng chưa vào
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit)
VALUES ('RF-000107', GETDATE(), GETDATE(), DATEADD(DAY, 3, GETDATE()), 'EMP-000001', 'T1101', 'CUS-000001', 500000);
GO

--Phiếu 2: đã checkin, có 1 bảng ghi trong HistoryCheckIn,
-- 1 bảng ghi RomReservationDetail và cập trạng thái phòng
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit)
VALUES ('RF-000108', DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, 3, GETDATE()), 'EMP-000002', 'T1105', 'CUS-000002', 500000);

INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
VALUES ('HCI-000001', DATEADD(DAY, -1, GETDATE()), 'RF-000108', 'EMP-000002');

INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
VALUES
    ('RRD-000001', DATEADD(DAY, -1, GETDATE()), 'T1105', 'RF-000108', 'EMP-000002');

UPDATE Room
SET roomStatus = 'ON_USE'
WHERE roomID = 'T1105';
GO


--Phiếu 3: đã checkin, có 1 bảng trong HistoryCheckIn,
-- 1 bảng ghi RomReservationDetail và cập trạng thái phòng
-- đã quá hạn Checkout 1 tiếng trước
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit)
VALUES ('RF-000109', DATEADD(DAY, -5, GETDATE()), DATEADD(DAY, -4, GETDATE()), DATEADD(HOUR, -1, GETDATE()), 'EMP-000003', 'V2102', 'CUS-000003', 500000);

INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
VALUES ('HCI-000002', DATEADD(DAY, -4, GETDATE()), 'RF-000109', 'EMP-000003');

INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
VALUES
    ('RRD-000002', DATEADD(DAY, -4, GETDATE()), 'V2102', 'RF-000109', 'EMP-000003');

UPDATE Room
SET roomStatus = 'OVERDUE'
WHERE roomID = 'V2102';
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
        RAISERROR(
            'Mỗi loại phòng chỉ được phép có 2 bản ghi trong Pricing (1 DAY và 1 HOUR)',
            16,
            1
        );
        ROLLBACK TRANSACTION;
    END
END;
GO


