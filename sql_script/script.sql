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
GO

-- Tạo bảng Account
CREATE TABLE Account (
    accountID VARCHAR(10) PRIMARY KEY,  
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
GO

-- Thêm dữ liệu vào bảng Account
INSERT INTO Account (accountID, userName, password, status, employeeID)
VALUES 
('ACC-000001', 'huynhducphu', 'test123@', 'ACTIVE', 'EMP-000001'),
('ACC-000002', 'nguyenxuanchuc', 'test123@', 'ACTIVE', 'EMP-000002'),
('ACC-000003', 'tranlegiahuy', 'test123@', 'ACTIVE', 'EMP-000003'),
('ACC-000004', 'dangnguyentienphat', 'test123@', 'ACTIVE', 'EMP-000004'),
('ACC-000005', 'vubahai', 'test123@', 'ACTIVE', 'EMP-000005');


GO

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
GO

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

--Tạo bảng ShiftAssignment
CREATE TABLE ShiftAssignment (
    shiftAssignmentId VARCHAR(10) NOT NULL PRIMARY KEY, -- Đặt kiểu dữ liệu và độ dài tùy theo yêu cầu của bạn
    description NVARCHAR(50), -- Mô tả có thể dài, sử dụng kiểu TEXT
    shiftId VARCHAR(15) NOT NULL, -- Khóa ngoại tới bảng Shift
    employeeId VARCHAR(10) NOT NULL, -- Khóa ngoại tới bảng Employee
    FOREIGN KEY (shiftId) REFERENCES Shift(shiftId), -- Liên kết tới bảng Shift
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId) -- Liên kết tới bảng Employee
);
GO

--Thêm dữ liệu vào bảng ShiftAssignment
INSERT INTO ShiftAssignment (shiftAssignmentId, description, shiftId, employeeId) VALUES
('SA-000001', 'Assigned to morning shift', 'SHIFT-AM-0001', 'EMP-000001'),
('SA-000002', 'Assigned to night shift', 'SHIFT-PM-0002', 'EMP-000002'),
('SA-000003', 'Assigned to night shift', 'SHIFT-PM-0003', 'EMP-000003');
GO

--Tạo bảo ServiceCategory
CREATE TABLE ServiceCategory (
    serviceCategoryID VARCHAR(10) NOT NULL PRIMARY KEY,
    serviceCategoryName NVARCHAR(50) NOT NULL
)
Go

--Thêm dữ liệu vào bảng ServiceCategory
INSERT INTO ServiceCategory (serviceCategoryID, serviceCategoryName)
VALUES
('SC-000001', 'Giai Tri'),  
('SC-000002', 'An Uong'),        
('SC-000003', 'Cham Soc & Suc Khoe'),   
('SC-000004', 'Van Chuyen'); 


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
-- Dịch vụ Giải trí
('HS-000001', 'Dich vu Karaoke', 'Phong karaoke chat luong cao, tieu chuan giai tri gia dinh', 100.00, 'SC-000001'),
('HS-000002', 'Ho boi', 'Su dung ho boi ngoai troi cho khach nghi', 50.00, 'SC-000001'),
-- Dịch vụ Ăn uống
('HS-000003', 'Bua sang tu chon', 'Bua sang buffet voi da dang mon an', 30.00, 'SC-000002'),
('HS-000004', 'Thuc uong tai phong', 'Phuc vu thuc uong tai phong', 20.00, 'SC-000002'),
-- Dịch vụ Chăm sóc & Sức khỏe
('HS-000005', 'Dich vu Spa', 'Massage toan than va lieu trinh cham soc da', 120.00, 'SC-000003'),
('HS-000006', 'Cham soc tre em', 'Su dung phong gym day du trang thiet bi', 80.00, 'SC-000003'),
-- Dịch vụ Vận chuyển
('HS-000007', 'Thue xe', 'Thue xe di chuyen trong thanh pho', 150.00, 'SC-000004');

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

GO

--Tạo bảng RoomCategory
CREATE TABLE RoomCategory (
    roomCategoryID VARCHAR(10) NOT NULL PRIMARY KEY,  -- Mã định danh của loại phòng
    roomCategoryName NVARCHAR(50) NOT NULL,  -- Tên của loại phòng, không được rỗng và không vượt quá 30 ký tự
    numberOfBed INT NOT NULL  -- Số lượng giường, phải ít nhất là 1
);
GO

--Thêm dữ liệu vào bảng RoomCategory
INSERT INTO RoomCategory (roomCategoryID, roomCategoryName, numberOfBed)
VALUES 
('RC-000001', 'Phong Thuong Giuong Don', 1),
('RC-000002', 'Phong Thuong Giuong Doi', 2),
('RC-000003', 'Phong Thuong VIP Don', 1),
('RC-000004', 'Phong Thuong VIP Doi', 2)
GO

--Tạo bảng pricing
CREATE TABLE Pricing (
    pricingID VARCHAR(10) NOT NULL PRIMARY KEY,  -- ID định giá
    priceUnit VARCHAR(15) NOT NULL CHECK (priceUnit IN ('DAY', 'HOUR')),  -- Đơn vị tính giá
    price MONEY NOT NULL,  -- Mức giá
	roomCategoryID VARCHAR(10) NOT NULL,
	CONSTRAINT fk_roomcategory_pricing FOREIGN KEY (roomCategoryID) REFERENCES RoomCategory(roomCategoryID)  -- Thiết lập khóa ngoại
);
GO

--Thêm dữ liệu vào bảng Pricing
INSERT INTO Pricing (pricingID, priceUnit, price, roomCategoryID)
VALUES 
	('P-000001', 'HOUR', 150000.00, 'RC-000001'), --Thường Đơn Giờ
	('P-000002', 'DAY', 800000.00, 'RC-000001'), --Thường Đơn Ngày
	('P-000003', 'HOUR', 200000.00, 'RC-000002'), --Thường Đôi Giờ
	('P-000004', 'DAY', 850000.00, 'RC-000002'), --Thường Đôi Ngày
	('P-000005', 'HOUR', 300000.00, 'RC-000003'), --VIP Đơn Giờ
	('P-000006', 'DAY', 1600000.00, 'RC-000003'), --VIP Đơn Ngày
	('P-000007', 'HOUR', 400000.00, 'RC-000004'), --VIP Đôi Giờ
	('P-000008', 'DAY', 1800000.00, 'RC-000004') --VIP Đôi Ngày
GO

--Tạo bảng Room
CREATE TABLE Room (
    roomID VARCHAR(10) NOT NULL PRIMARY KEY,  
	-- Mã phòng
    roomStatus VARCHAR(20) NOT NULL CHECK (roomStatus IN ('AVAILABLE', 'ON_USE', 'UNAVAILABLE')),  
	-- Trạng thái của phòng
    dateOfCreation DATETIME NOT NULL,  
	-- Thời gian tạo phòng
    roomCategoryID VARCHAR(10) NOT NULL,  
	-- Khóa ngoại liên kết đến bảng RoomCategory
    CONSTRAINT fk_roomCategory FOREIGN KEY (roomCategoryID) REFERENCES RoomCategory(roomCategoryID)  -- Thiết lập khóa ngoại
);
GO

--Thêm dữ liệu vào bảng Room
INSERT INTO Room (roomID, roomStatus, dateOfCreation, roomCategoryID)
VALUES 
	('T1101', 'ON_USE', '2024-09-28 10:00:00', 'RC-000001'),  
	('T2102', 'ON_USE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1103', 'ON_USE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2104', 'ON_USE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1105', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2106', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1107', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2108', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1109', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2110', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1211', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2212', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1213', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2214', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1215', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2216', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1217', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2218', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1219', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2220', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1321', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2322', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1323', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2324', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1325', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2326', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1327', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2328', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1329', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2330', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1431', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2432', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1433', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2434', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1435', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2436', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1437', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2438', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1439', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2440', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1541', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2542', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1543', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2544', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1545', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2546', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002'),
	('V1547', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000003'),
	('V2548', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
	('T1549', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
	('T2550', 'AVAILABLE', '2024-09-28 10:00:00', 'RC-000002')
GO

--Tạo bảng ReservationForm
CREATE TABLE ReservationForm (
    reservationFormID VARCHAR(10) NOT NULL PRIMARY KEY,        
	-- Mã đặt phòng duy nhất
    reservationDate DATETIME NOT NULL,            
	-- Ngày đặt phòng
    approxCheckInDate DATETIME NOT NULL,          
	-- Ngày dự kiến nhận phòng
    approxCheckOutDate DATETIME NOT NULL,         
	-- Ngày dự kiến trả phòng
    employeeID VARCHAR(10) NOT NULL,              
	-- ID nhân viên liên quan đến đặt phòng
    roomID VARCHAR(10) NOT NULL,                   
	-- Mã phòng đặt
    customerID VARCHAR(10) NOT NULL,               
	-- ID khách hàng đặt phòng

    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID),  
	-- Khóa ngoại liên kết đến bảng Employee
    FOREIGN KEY (roomID) REFERENCES Room(roomID),              
	-- Khóa ngoại liên kết đến bảng Room
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)   
	-- Khóa ngoại liên kết đến bảng Customer
);
GO

--Thêm dữ liệu vào bảng ReservationForm
INSERT INTO ReservationForm (reservationFormID, reservationDate, approxCheckInDate, approxCheckOutDate, employeeID, roomID, customerID)
VALUES 
('RF-000001', '2024-09-29 10:00:00', '2024-10-02 14:00:00', '2024-10-05 12:00:00', 'EMP-000001', 'T1101', 'CUS-000001'),
('RF-000002', '2024-09-29 11:30:00', '2024-10-03 15:00:00', '2024-10-07 11:00:00', 'EMP-000002', 'T2102', 'CUS-000002'),
('RF-000003', '2024-09-29 09:00:00', '2024-10-04 13:00:00', '2024-10-06 10:00:00', 'EMP-000003', 'V1103', 'CUS-000003'),
('RF-000004', '2024-09-29 08:00:00', '2024-10-05 12:00:00', '2024-10-08 09:00:00', 'EMP-000004', 'V2104', 'CUS-000004');
GO

-- Tạo bảng RoomUsageService với khóa ngoại liên kết với ReservationForm
CREATE TABLE RoomUsageService (
    roomUsageServiceId VARCHAR(10) NOT NULL PRIMARY KEY,  
    -- Mã định danh của việc sử dụng dịch vụ phòng

    quantity INT NOT NULL,  
    -- Số lượng dịch vụ đã sử dụng

    hotelServiceId VARCHAR(10) NOT NULL,  
    -- Mã dịch vụ khách sạn

    reservationFormID VARCHAR(10) NOT NULL,  
    -- Mã đặt phòng, liên kết đến bảng ReservationForm

    FOREIGN KEY (hotelServiceId) REFERENCES HotelService(hotelServiceId),  
    -- Khóa ngoại liên kết đến bảng HotelService

    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  
    -- Khóa ngoại liên kết đến bảng ReservationForm
);
GO

-- Thêm dữ liệu vào bảng RoomUsageService
INSERT INTO RoomUsageService (roomUsageServiceId, quantity, hotelServiceId, reservationFormID)
VALUES 
    ('RUS-000001', 2, 'HS-000001', 'RF-000001'),  
    ('RUS-000002', 1, 'HS-000002', 'RF-000002'),  
    ('RUS-000003', 3, 'HS-000003', 'RF-000003'),  
    ('RUS-000004', 1, 'HS-000004', 'RF-000004');
GO

CREATE TABLE RoomReservationDetail (
    roomReservationDetailID VARCHAR(10) NOT NULL PRIMARY KEY,   
    -- Mã định danh của chi tiết đặt phòng

    dateChanged DATETIME NOT NULL,  
    -- Ngày thay đổi thông tin liên quan đến phòng

    roomID VARCHAR(10) NOT NULL,    
    -- Khóa ngoại liên kết đến bảng Room

    reservationFormID VARCHAR(10) NOT NULL,  
    -- Khóa ngoại liên kết đến bảng ReservationForm

    FOREIGN KEY (roomID) REFERENCES Room(roomID),  
    -- Khóa ngoại liên kết đến bảng Room

    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  
    -- Khóa ngoại liên kết đến bảng ReservationForm
);
GO

-- Thêm dữ liệu vào bảng RoomReservationDetail
INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID)
VALUES 
    ('RRD-000001', '2024-10-02 14:00:00', 'T1101', 'RF-000001'),
    ('RRD-000002', '2024-10-03 15:00:00', 'T2102', 'RF-000002'),
    ('RRD-000003', '2024-10-04 13:00:00', 'V1103', 'RF-000003'),
    ('RRD-000004', '2024-10-05 12:00:00', 'V2104', 'RF-000004');
GO

CREATE TABLE Tax (
    taxID VARCHAR(10) NOT NULL PRIMARY KEY,          
    -- Mã định danh của loại thuế

    taxName NVARCHAR(50) NOT NULL,                  
    -- Tên loại thuế (VD: VAT, Service Tax)

    taxRate DECIMAL(5, 2) NOT NULL,                          
    -- Tỷ lệ thuế (tính theo %)

    dateOfCreation DATE NOT NULL,                    
    -- Ngày tạo loại thuế

    activate BIT NOT NULL                             
    -- Trạng thái kích hoạt (1: kích hoạt, 0: không kích hoạt)
);
GO

-- Thêm dữ liệu vào bảng Tax
INSERT INTO Tax (taxID, taxName, taxRate, dateOfCreation, activate)
VALUES
    ('TAX-0001', 'VAT', 10.0, '2024-10-01', 1),  -- VAT với 10% thuế
    ('TAX-0002', 'Service Tax', 5.0, '2024-10-01', 1);  -- Thuế dịch vụ với 5%
GO


--Tạo bảng HistoryCheckin
CREATE TABLE HistoryCheckin (
    historyCheckInID VARCHAR(10) NOT NULL PRIMARY KEY,        
	-- Mã lịch sử nhận phòng
    checkInDate DATETIME NOT NULL,                            
	-- Ngày giờ nhận phòng
    reservationFormID VARCHAR(10) NOT NULL,                       
	-- Khóa ngoại liên kết đến phiếu đặt phòng

    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)  -- Thiết lập khóa ngoại
);
GO

CREATE TABLE Invoice (
    invoiceID VARCHAR(10) NOT NULL PRIMARY KEY,        
    -- Mã hóa đơn duy nhất

    invoiceDate DATETIME NOT NULL,                     
    -- Ngày giờ lập hóa đơn

    roomCharge DECIMAL(18, 2) NOT NULL,                          
    -- Tổng tiền phòng

    servicesCharge DECIMAL(18, 2) NOT NULL,                      
    -- Tổng tiền dịch vụ

    totalDue DECIMAL(18, 2) NOT NULL,                            
    -- Tổng tiền phải trả trước thuế

    netDue DECIMAL(18, 2) NOT NULL,                              
    -- Số tiền thực tế phải trả (sau thuế và các khoản khác)

    taxID VARCHAR(10) NOT NULL,                         
    -- Khóa ngoại liên kết đến bảng Tax để biết loại thuế áp dụng

    reservationFormID VARCHAR(10) NOT NULL,             
    -- Khóa ngoại liên kết đến bảng ReservationForm để biết hóa đơn thuộc phiếu đặt phòng nào

    FOREIGN KEY (taxID) REFERENCES Tax(taxID),          
    -- Khóa ngoại liên kết đến bảng Tax

    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID) 
    -- Khóa ngoại liên kết đến bảng ReservationForm
);
GO

-- Thêm dữ liệu vào bảng Invoice
INSERT INTO Invoice (invoiceID, invoiceDate, roomCharge, servicesCharge, totalDue, netDue, taxID, reservationFormID)
VALUES
    ('INV-0001', '2024-10-05 12:00:00', 800000, 150000, 950000, 1045000, 'TAX-0001', 'RF-000001'),
    ('INV-0002', '2024-10-07 13:00:00', 850000, 200000, 1050000, 1155000, 'TAX-0002', 'RF-000002');
GO

--Thêm dữ liệu vào bảng HistoryCheckin
INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID)
VALUES 
    ('HCI-000001', '2024-10-03 10:00:00', 'RF-000001'),
    ('HCI-000002', '2024-10-04 09:00:00', 'RF-000002'),
    ('HCI-000003', '2024-10-05 08:00:00', 'RF-000003'),
    ('HCI-000004', '2024-10-06 07:00:00', 'RF-000004');
GO

-- Tạo bảng HistoryCheckOut
CREATE TABLE HistoryCheckOut (
    historyCheckOutID VARCHAR(10) NOT NULL PRIMARY KEY,      
    -- Mã định danh của lịch sử check-out

    checkOutDate DATETIME NOT NULL,                         
    -- Ngày giờ khi check-out diễn ra

    reservationFormID VARCHAR(10) NOT NULL,                
    -- Khóa ngoại liên kết đến phiếu đặt phòng

    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID) 
    -- Thiết lập khóa ngoại liên kết đến phiếu đặt phòng
);
GO

-- Thêm dữ liệu vào bảng HistoryCheckOut
INSERT INTO HistoryCheckOut (historyCheckOutID, checkOutDate, reservationFormID)
VALUES 
    ('HCO-000001', '2024-10-05 11:00:00', 'RF-000001'),
    ('HCO-000002', '2024-10-06 10:00:00', 'RF-000002'),
    ('HCO-000003', '2024-10-07 09:00:00', 'RF-000003'),
    ('HCO-000004', '2024-10-08 08:00:00', 'RF-000004');
GO




