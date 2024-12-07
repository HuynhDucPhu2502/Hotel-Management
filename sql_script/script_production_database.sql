-- Kiểm tra và xóa cơ sở dữ liệu nếu tồn tại
USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = 'HotelDatabase')
BEGIN
    DROP DATABASE HotelDatabase;
END
GO

-- Tạo cơ sở dữ liệu mới
CREATE DATABASE HotelDatabase;
GO

-- Sử dụng cơ sở dữ liệu HotelDatabase
USE HotelDatabase;
GO

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
    position NVARCHAR(15) NOT NULL CHECK (position IN ('RECEPTIONIST', 'MANAGER')),
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE')),
	avatar NVARCHAR(MAX) default 'iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABRElEQVRIieXVSU/CQAAF4P7/EyfE4MkAmiBcBEwgokhAIwGUxi40gGA3QAmdtudnW1MMURjpcvLwkqYzed/MNJMy9kpBnGH+EfDxBmsxgr2cOM9ydIClCSBcBaSfA+llvZj9c1hi7QsMA1gav1X8I8952ItxcIDwld3l/m6kmxDAvtX7wKAYL0AGF3HvoBAcMNkCHeDKIQD+ig4MQ3xka9ymAtasEwLQBTpAuWyUmyyDsMXdwMvl3vI/AArWUvP38m4W61E7PEAUAbN6Ckb3u9zoZfBaS8KQuSgAzgGOIZYTHjStH0EsJTC9TnljgQFTFbES76C3M1AbJ1AaaW/Vk2oSym3ae+eOuXPcuVTA1CXnTDt4Z6ub0kOitU6xfCphNWxt7YwxVQHzx/zBhbS4nabzL2HmD7nIyzfI/RmYuMr9xA58As9AE1gjSRApAAAAAElFTkSuQmCC'
);
GO

-- Tạo bảng Account
CREATE TABLE Account (
    accountID NVARCHAR(15) PRIMARY KEY,
    userName NVARCHAR(20) NOT NULL,
    password NVARCHAR(64) NOT NULL,
    status NVARCHAR(10) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED')),
    employeeID NVARCHAR(15) NOT NULL,
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
);
GO

-- Tạo bảng ServiceCategory
CREATE TABLE ServiceCategory (
    serviceCategoryID NVARCHAR(15) NOT NULL PRIMARY KEY,
    serviceCategoryName NVARCHAR(50) NOT NULL,
	icon NVARCHAR(255),
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
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
		ON UPDATE CASCADE,

	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
);
GO

-- Tạo bảng RoomCategory
CREATE TABLE RoomCategory (
    roomCategoryID NVARCHAR(15) NOT NULL PRIMARY KEY,
    roomCategoryName NVARCHAR(50) NOT NULL,
    numberOfBed INT NOT NULL,
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
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
    FOREIGN KEY (roomCategoryID) REFERENCES RoomCategory(roomCategoryID),
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
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
    FOREIGN KEY (shiftId) REFERENCES Shift(shiftID) ON UPDATE CASCADE,
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
    dob DATE NOT NULL,
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
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
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID),
    FOREIGN KEY (roomID) REFERENCES Room(roomID),
    FOREIGN KEY (customerID) REFERENCES Customer(customerID),
	isActivate NVARCHAR(10) NOT NULL DEFAULT 'ACTIVATE' CHECK (isActivate IN ('ACTIVATE', 'DEACTIVATE'))
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

-- Tạo bảng RoomUsageService
CREATE TABLE RoomUsageService (
    roomUsageServiceId NVARCHAR(15) NOT NULL PRIMARY KEY,
    quantity INT NOT NULL,
    unitPrice DECIMAL(18, 2) NOT NULL,
    totalPrice AS (quantity * unitPrice) PERSISTED,
    dateAdded DATETIME NOT NULL,
    hotelServiceId NVARCHAR(15) NOT NULL,
    reservationFormID NVARCHAR(15) NOT NULL,
    employeeID NVARCHAR(15),

    FOREIGN KEY (hotelServiceId) REFERENCES HotelService(hotelServiceId),
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
		ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- Tạo bảng HistoryCheckin
CREATE TABLE HistoryCheckin (
    historyCheckInID NVARCHAR(15) NOT NULL PRIMARY KEY,
    checkInDate DATETIME NOT NULL,
    reservationFormID NVARCHAR(15) NOT NULL,
    employeeID NVARCHAR(15),
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
    employeeID NVARCHAR(15),
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID),
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
		ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- Tạo bảng Invoice
CREATE TABLE Invoice (
    invoiceID NVARCHAR(15) NOT NULL PRIMARY KEY,
    invoiceDate DATETIME NOT NULL,
    roomCharge DECIMAL(18, 2) NOT NULL,
    servicesCharge DECIMAL(18, 2) NOT NULL,
    totalDue AS (roomCharge + servicesCharge) PERSISTED,
    netDue AS ((roomCharge + servicesCharge) * 1.1) PERSISTED,
    reservationFormID NVARCHAR(15) NOT NULL,
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)
);
GO

-- Tạo bảng RoomDialog
CREATE TABLE RoomDialog (
    dialogID INT IDENTITY(1,1) PRIMARY KEY,
    roomID NVARCHAR(15) NULL,
    reservationFormID NVARCHAR(15) NULL,
    dialog NVARCHAR(200) NOT NULL,
    dialogType NVARCHAR(15) NOT NULL CHECK (dialogType IN ('TRANSFER', 'RESERVATION', 'CHECKIN', 'CHECKOUT', 'SERVICE')),
    timestamp DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (roomID) REFERENCES Room(roomID)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (reservationFormID) REFERENCES ReservationForm(reservationFormID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

CREATE TABLE Notifications (
    NotificationID INT IDENTITY PRIMARY KEY, -- ID duy nhất của thông báo
    EntityID VARCHAR(10),                    -- (Tùy chọn) ID của người nhận thông báo
    Title NVARCHAR(125),                      -- Tiêu đề thông báo
    Content NVARCHAR(255),                   -- Nội dung thông báo
    CreatedAt DATETIME DEFAULT GETDATE(), -- Thời điểm tạo thông báo
    IsRead BIT DEFAULT 0                  -- Trạng thái đã đọc
);
GO

-- ===================================================================================
-- 2. TRIGGER - FUNCTION - STORE PROCEDURE
-- ===================================================================================

-- Tạo hàm băm mật khẩu
CREATE FUNCTION dbo.HashPassword (@password NVARCHAR(30))
RETURNS NVARCHAR(64)
AS
BEGIN
    DECLARE @hashed NVARCHAR(64)
    SET @hashed = CONVERT(NVARCHAR(64), HASHBYTES('SHA2_256', @password), 2)
    RETURN @hashed
END
GO

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

-- Tạo procedure cho quy trình thêm phiếu đặt phòng
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE CreatingReservationForm
    @checkInDate DATETIME,
    @checkOutDate DATETIME,
    @employeeID NVARCHAR(20),
    @roomID NVARCHAR(20),
    @customerID NVARCHAR(20),
    @roomBookingDeposit FLOAT,
    @message NVARCHAR(255) OUTPUT
AS
BEGIN
    DECLARE @reservationCount INT = 0;
    DECLARE @idCardCount INT = 0;
    DECLARE @dialogMessage NVARCHAR(255);
    DECLARE @reservationFormID NVARCHAR(15);

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Kiểm tra trùng ngày đặt phòng
        SELECT @reservationCount = COUNT(*)
        FROM ReservationForm
        WHERE roomID = @roomID
          AND @checkInDate < checkOutDate
          AND @checkOutDate > checkInDate;

        IF @reservationCount > 0
        BEGIN
            SET @message = 'CREATING_RESERVATION_FORM_CHECK_DATE_OVERLAP';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Kiểm tra trùng số CCCD
        SELECT @idCardCount = COUNT(*)
        FROM ReservationForm rf
        INNER JOIN Customer c ON rf.customerID = c.customerID
        WHERE c.customerID = @customerID
          AND @checkInDate < rf.checkOutDate
          AND @checkOutDate > rf.checkInDate;

        IF @idCardCount > 0
        BEGIN
            SET @message = 'CREATING_RESERVATION_FORM_ID_CARD_NUMBER_OVERLAP';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Lấy nextID từ GlobalSequence
        SELECT @reservationFormID = nextID
        FROM GlobalSequence
        WHERE tableName = 'ReservationForm';

        -- Thêm dữ liệu vào bảng ReservationForm
        INSERT INTO ReservationForm (
            reservationFormID, reservationDate, checkInDate, checkOutDate,
            employeeID, roomID, customerID, roomBookingDeposit
        )
        VALUES (
            @reservationFormID, GETDATE(), @checkInDate, @checkOutDate,
            @employeeID, @roomID, @customerID, @roomBookingDeposit
        );

        -- Tạo message cho RoomDialog
        SET @dialogMessage = N'Đặt phòng cho ' + @customerID + N' từ '
                            + CONVERT(NVARCHAR, @checkInDate, 103) + N' đến '
                            + CONVERT(NVARCHAR, @checkOutDate, 103);

        -- Thêm dữ liệu vào RoomDialog
        INSERT INTO RoomDialog (
            roomID, reservationFormID, dialog, dialogType, timestamp
        )
        VALUES (
            @roomID, @reservationFormID, @dialogMessage, 'RESERVATION', GETDATE()
        );

        COMMIT TRANSACTION;
        SET @message = 'CREATING_RESERVATION_FORM_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END
GO

-- Tạo procedure đặt dịch vụ phòng
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE ServiceOrdering
    @quantity INT,
    @unitPrice DECIMAL(18, 2),
    @serviceID NVARCHAR(20),
    @reservationFormID NVARCHAR(15),
    @employeeID NVARCHAR(15),
    @dateAdded DATETIME,
    @message NVARCHAR(255) OUTPUT
AS
BEGIN
    DECLARE @dialogMessage NVARCHAR(255);
    DECLARE @roomUsageServiceID NVARCHAR(15);
    DECLARE @serviceName NVARCHAR(50);

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Kiểm tra nếu số lượng nhỏ hơn hoặc bằng 0
        IF @quantity <= 0
        BEGIN
            SET @message = 'SERVICE_ORDERING_INVALID_QUANTITY';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Lấy nextID từ GlobalSequence
        SELECT @roomUsageServiceID = nextID
        FROM GlobalSequence
        WHERE tableName = 'RoomUsageService';

        -- Lấy serviceName từ HotelService
        SELECT @serviceName = serviceName
        FROM HotelService
        WHERE hotelServiceId = @serviceID;

        -- Thêm dữ liệu vào bảng RoomUsageService
        INSERT INTO RoomUsageService (
            roomUsageServiceId, reservationFormID, hotelServiceId, quantity,
            unitPrice, dateAdded, employeeID
        )
        VALUES (
            @roomUsageServiceID, @reservationFormID, @serviceID, @quantity,
            @unitPrice, @dateAdded, @employeeID
        );

        -- Tạo message cho RoomDialog
        SET @dialogMessage = N'Đặt dịch vụ x' + CONVERT(NVARCHAR, @quantity) + N' '
                            + @serviceName + N' ' + @serviceID;

        -- Thêm dữ liệu vào RoomDialog
        INSERT INTO RoomDialog (
            roomID, reservationFormID, dialog, dialogType, timestamp
        )
        VALUES (
            (SELECT roomID FROM ReservationForm WHERE reservationFormID = @reservationFormID),
            @reservationFormID, @dialogMessage, 'SERVICE', GETDATE()
        );

        COMMIT TRANSACTION;
        SET @message = 'SERVICE_ORDERING_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END
GO

-- Tạo procedure chuyển phòng
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE RoomChanging
    @currentRoomID NVARCHAR(20),
    @newRoomID NVARCHAR(20),
    @reservationFormID NVARCHAR(15),
    @employeeID NVARCHAR(15),
    @message NVARCHAR(255) OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Kiểm tra nếu phiếu đặt phòng không tồn tại hoặc đã quá hạn
        IF NOT EXISTS (
            SELECT 1
            FROM ReservationForm
            WHERE reservationFormID = @reservationFormID
              AND checkOutDate >= GETDATE()
        )
        BEGIN
            SET @message = 'ROOM_CHANGING_RESERVATION_NOT_FOUND_OR_EXPIRED';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Kiểm tra trạng thái phòng mới có khả dụng hay không
        IF NOT EXISTS (
            SELECT 1
            FROM Room
            WHERE roomID = @newRoomID
              AND roomStatus = 'AVAILABLE'
              AND isActivate = 'ACTIVATE'
        )
        BEGIN
            SET @message = 'ROOM_CHANGING_NEW_ROOM_NOT_AVAILABLE';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Cập nhật trạng thái phòng hiện tại thành AVAILABLE
        UPDATE Room
        SET roomStatus = 'AVAILABLE'
        WHERE roomID = @currentRoomID;

        -- Cập nhật reservationForm với roomID mới
        UPDATE ReservationForm
        SET roomID = @newRoomID
        WHERE reservationFormID = @reservationFormID;

        -- Cập nhật trạng thái phòng mới thành ON_USE
        UPDATE Room
        SET roomStatus = 'ON_USE'
        WHERE roomID = @newRoomID;

        -- Thêm bản ghi vào RoomReservationDetail và chọn nextID trực tiếp
        INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
        VALUES (
            (SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomReservationDetail'),
            GETDATE(),
            @newRoomID,
            @reservationFormID,
            @employeeID
        );

        -- Tạo RoomDialog cho phòng hiện tại
        INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
        VALUES (
            @currentRoomID,
            @reservationFormID,
            CONCAT(N'Phòng ', @currentRoomID, N' đã chuyển sang phòng ', @newRoomID),
            'TRANSFER',
            GETDATE()
        );

        -- Tạo RoomDialog cho phòng mới
        INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
        VALUES (
            @newRoomID,
            @reservationFormID,
            CONCAT(N'Phòng ', @newRoomID, N' đã nhận chuyển từ phòng ', @currentRoomID),
            'TRANSFER',
            GETDATE()
        );

        COMMIT TRANSACTION;

        SET @message = 'ROOM_CHANGING_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END
GO

-- Tạo procedure nhận phòng
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE RoomCheckingIn(
    @reservationFormID NVARCHAR(15),
    @employeeID NVARCHAR(15),
    @message NVARCHAR(255) OUTPUT
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @roomID NVARCHAR(15);
        DECLARE @currentTime DATETIME = GETDATE();
        DECLARE @roomReservationDetailID NVARCHAR(15);
        DECLARE @historyCheckInID NVARCHAR(15);
        DECLARE @roomDialogID NVARCHAR(15);

        -- Kiểm tra tồn tại và thời gian hợp lệ của reservationForm
        IF NOT EXISTS (
            SELECT 1
            FROM ReservationForm
            WHERE reservationFormID = @reservationFormID
              AND @currentTime BETWEEN checkInDate AND DATEADD(HOUR, 2, checkInDate)
              AND NOT EXISTS (
                  SELECT 1
                  FROM HistoryCheckin
                  WHERE reservationFormID = @reservationFormID
              )
        )
        BEGIN
            SET @message = 'ROOM_CHECKING_IN_INVALID_RESERVATION';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Lấy thông tin roomID từ ReservationForm
        SELECT @roomID = roomID
        FROM ReservationForm
        WHERE reservationFormID = @reservationFormID;

        -- Lấy nextID từ GlobalSequence
        SELECT @roomReservationDetailID = nextID
        FROM GlobalSequence
        WHERE tableName = 'RoomReservationDetail';

        SELECT @historyCheckInID = nextID
        FROM GlobalSequence
        WHERE tableName = 'HistoryCheckin';

        SELECT @roomDialogID = nextID
        FROM GlobalSequence
        WHERE tableName = 'RoomDialog';

        -- Thêm dữ liệu vào bảng HistoryCheckin
        INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
        VALUES (@historyCheckInID, @currentTime, @reservationFormID, @employeeID);

        -- Thêm dữ liệu vào bảng RoomReservationDetail
        INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
        VALUES (@roomReservationDetailID, @currentTime, @roomID, @reservationFormID, @employeeID);

        -- Thêm dữ liệu vào bảng RoomDialog
        INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
        VALUES (
            @roomID,
            @reservationFormID,
            CONCAT(N'Check-in tại phòng ', @roomID),
            'CHECKIN',
            @currentTime
        );

        -- Cập nhật trạng thái phòng thành ON_USE
        UPDATE Room
        SET roomStatus = 'ON_USE'
        WHERE roomID = @roomID;

        COMMIT TRANSACTION;
        SET @message = 'ROOM_CHECKING_IN_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END;
GO

-- Tạo procedure nhận phòng sớm
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE RoomEarlyCheckingIn(
    @reservationFormID NVARCHAR(15),
    @employeeID NVARCHAR(15),
    @message NVARCHAR(255) OUTPUT
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @roomID NVARCHAR(15);
        DECLARE @currentTime DATETIME = GETDATE();
        DECLARE @checkInTime DATETIME;
        DECLARE @earlyCheckInStart DATETIME;
        DECLARE @earlyCheckInEnd DATETIME;
        DECLARE @roomReservationDetailID NVARCHAR(15);
        DECLARE @historyCheckInID NVARCHAR(15);
        DECLARE @roomDialogID NVARCHAR(15);
        DECLARE @roomBookingDeposit FLOAT;

        -- Lấy thời gian check-in từ ReservationForm
        SELECT @checkInTime = checkInDate
        FROM ReservationForm
        WHERE reservationFormID = @reservationFormID;

        -- Xác định khoảng thời gian cho Early Check-In
        SET @earlyCheckInStart = DATEADD(MINUTE, -30, @checkInTime);
        SET @earlyCheckInEnd = DATEADD(SECOND, -10, @checkInTime);

        -- Kiểm tra thời gian hiện tại có hợp lệ để Early Check-In
        IF NOT (@currentTime BETWEEN @earlyCheckInStart AND @earlyCheckInEnd)
        BEGIN
            SET @message = 'ROOM_CHECKING_IN_TIME_INVALID';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Kiểm tra ReservationForm hợp lệ và chưa được check-in
        IF NOT EXISTS (
            SELECT 1
            FROM ReservationForm
            WHERE reservationFormID = @reservationFormID
              AND NOT EXISTS (
                  SELECT 1
                  FROM HistoryCheckin
                  WHERE reservationFormID = @reservationFormID
              )
        )
        BEGIN
            SET @message = 'ROOM_CHECKING_IN_INVALID_RESERVATION';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Lấy roomID từ ReservationForm
        SELECT @roomID = roomID
        FROM ReservationForm
        WHERE reservationFormID = @reservationFormID;

        -- Lấy nextID từ GlobalSequence
        SELECT @roomReservationDetailID = nextID
        FROM GlobalSequence
        WHERE tableName = 'RoomReservationDetail';

        SELECT @historyCheckInID = nextID
        FROM GlobalSequence
        WHERE tableName = 'HistoryCheckin';

        SELECT @roomDialogID = nextID
        FROM GlobalSequence
        WHERE tableName = 'RoomDialog';

        -- Thêm dữ liệu vào bảng HistoryCheckin
        INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
        VALUES (@historyCheckInID, @currentTime, @reservationFormID, @employeeID);

        -- Thêm dữ liệu vào bảng RoomReservationDetail
        INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
        VALUES (@roomReservationDetailID, @currentTime, @roomID, @reservationFormID, @employeeID);

        -- Lấy RoomBookingDeposit từ ReservationForm
        SELECT @roomBookingDeposit = RoomBookingDeposit
        FROM ReservationForm
        WHERE reservationFormID = @reservationFormID;

        -- Cập nhật checkInDate và RoomBookingDeposit trong một câu lệnh
        UPDATE ReservationForm
        SET
            checkInDate = @currentTime,
            RoomBookingDeposit = @roomBookingDeposit + 50000
        WHERE reservationFormID = @reservationFormID;

        -- Thêm dữ liệu vào bảng RoomDialog
        INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
        VALUES (
            @roomID,
            @reservationFormID,
            CONCAT(N'Check-in sớm tại phòng ', @roomID),
            'CHECKIN',
            @currentTime
        );

        -- Cập nhật trạng thái phòng thành ON_USE
        UPDATE Room
        SET roomStatus = 'ON_USE'
        WHERE roomID = @roomID;

        COMMIT TRANSACTION;
        SET @message = 'ROOM_CHECKING_IN_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END;
GO

-- Tạo procedure trả phòng
-- (procedure không hỗ trợ sinh nextID mới)
CREATE PROCEDURE roomCheckingOut(
    @reservationFormID NVARCHAR(15),
    @employeeID NVARCHAR(15),
    @roomCharge MONEY,
    @serviceCharge MONEY,
    @message NVARCHAR(255) OUTPUT
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Kiểm tra phiếu đặt phòng hợp lệ
        IF NOT EXISTS (
            SELECT 1
            FROM ReservationForm
            WHERE reservationFormID = @reservationFormID
              AND isActivate = 'ACTIVATE'
        )
        BEGIN
            SET @message = 'RESERVATION_FORM_NOT_FOUND';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Lấy thông tin roomID từ ReservationForm
        DECLARE @roomID NVARCHAR(15);
        SELECT @roomID = roomID
        FROM ReservationForm
        WHERE reservationFormID = @reservationFormID;

        -- Lấy nextID từ GlobalSequence
        DECLARE @historyCheckOutID NVARCHAR(15);
        DECLARE @invoiceID NVARCHAR(15);

        SELECT @historyCheckOutID = nextID
        FROM GlobalSequence
        WHERE tableName = 'HistoryCheckOut';

        SELECT @invoiceID = nextID
        FROM GlobalSequence
        WHERE tableName = 'Invoice';

        -- Thêm dữ liệu vào bảng HistoryCheckOut
        INSERT INTO HistoryCheckOut (historyCheckOutID, checkOutDate, reservationFormID, employeeID)
        VALUES (@historyCheckOutID, GETDATE(), @reservationFormID, @employeeID);

        -- Thêm dữ liệu vào bảng Invoice
        INSERT INTO Invoice (invoiceID, invoiceDate, roomCharge, servicesCharge, reservationFormID)
        VALUES (@invoiceID, GETDATE(), @roomCharge, @serviceCharge, @reservationFormID);

        -- Cập nhật trạng thái phòng thành AVAILABLE
        UPDATE Room
        SET roomStatus = 'AVAILABLE'
        WHERE roomID = @roomID;

        -- Thêm dữ liệu vào bảng RoomDialog
        INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
        VALUES (
            @roomID,
            @reservationFormID,
            CONCAT(N'Check-out thành công cho phòng ', @roomID),
            'CHECKOUT',
            GETDATE()
        );

        COMMIT TRANSACTION;
        SET @message = 'ROOM_CHECKOUT_SUCCESS';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @message = ERROR_MESSAGE();
    END CATCH
END;
GO
-- ===================================================================================
-- 3. THÊM DỮ LIỆU
-- ===================================================================================

-- Thêm dữ liệu vào bảng GlobalSequence
INSERT INTO GlobalSequence(tableName, nextID)
VALUES
	('Employee', 'EMP-000006'),
	('Account', 'ACC-000006'),
	('ServiceCategory', 'SC-000005'),
	('HotelService', 'HS-000021'),
	('Pricing', 'P-000009'),
	('RoomCategory', 'RC-000005'),
	('ShiftAssignment', 'SA-000005'),
	('Customer', 'CUS-000031'),
	('ReservationForm', 'RF-000113'),
	('HistoryCheckIn', 'HCI-000005'),
	('HistoryCheckOut', 'HCO-000001'),
	('RoomReservationDetail', 'RRD-000005'),
	('RoomUsageService', 'RUS-000005'),
	('Invoice', 'INV-000101');
GO

-- Thêm dữ liệu vào bảng Employee
INSERT INTO Employee (employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position)
VALUES
    ('EMP-000000', N'ADMIN', '0912345678', 'quanlykhachsan@gmail.com', 'KHÔNG CÓ', 'MALE', '001099012346', '2000-01-01', 'MANAGER'),
    ('EMP-000001', N'Huynh Duc Phu', '0912345678', 'phuhuynh@gmail.com', N'123 Ho Chi Minh', 'MALE', '001099012345', '1985-06-15', 'MANAGER'),
    ('EMP-000002', N'Nguyen Xuan Chuc', '0908765432', 'chucnguyen@yahoo.com', N'456 Hue', 'MALE', '002199012346', '1990-04-22', 'RECEPTIONIST'),
    ('EMP-000003', N'Le Tran Gia Huy', '0987654321', 'huytranle@gmail.com', N'789 Ho Chi Minh', 'MALE', '003299012347', '1992-08-19', 'MANAGER'),
    ('EMP-000004', N'Dang Nguyen Tien Phat', '0934567890', 'phatnguyen@gmail.com', N'321 Binh Phuoc', 'MALE', '004399012348', '1987-12-05', 'RECEPTIONIST'),
    ('EMP-000005', N'Vu Ba Hai', '0923456789', 'vubachai@yahoo.com', N'654 Long An', 'MALE', '004399012349', '1995-03-30', 'MANAGER');
GO

-- Thêm dữ liệu vào bảng Account
INSERT INTO Account (accountID, userName, password, status, employeeID)
VALUES
    ('ACC-000001', N'huynhducphu', dbo.HashPassword(N'test123@'), N'ACTIVE', 'EMP-000001'),
    ('ACC-000002', N'nguyenxuanchuc', dbo.HashPassword(N'test123@'), N'ACTIVE', 'EMP-000002'),
    ('ACC-000003', N'letranle', dbo.HashPassword(N'test123@'), N'LOCKED', 'EMP-000003'),
    ('ACC-000004', N'tienphat', dbo.HashPassword(N'test123@'), N'INACTIVE', 'EMP-000004'),
    ('ACC-000005', N'vubahai', dbo.HashPassword(N'test123@'), N'ACTIVE', 'EMP-000005');
GO


-- Thêm dữ liệu vào bảng ServiceCategory
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
    ('V2102', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1203', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2304', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1105', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2206', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1307', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2408', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004'),
    ('T1109', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000001'),
    ('V2210', N'AVAILABLE', '2024-09-28 10:00:00', 'RC-000004');
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
    ('SA-000004', N'Assigned to morning shift', 'SHIFT-AM-0004', 'EMP-000004')
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
	('RF-000006', '2021-05-22 10:10:00', '2021-05-23 15:30:00', '2021-05-25 11:00:00', 'EMP-000001', 'V2210', 'CUS-000011', 1440000),
    ('RF-000007', '2021-06-15 13:35:00', '2021-06-16 09:00:00', '2021-06-18 10:30:00', 'EMP-000002', 'V2102', 'CUS-000001', 510000),
    ('RF-000008', '2021-07-05 12:40:00', '2021-07-06 08:45:00', '2021-07-08 12:30:00', 'EMP-000003', 'V2206', 'CUS-000016', 510000),
    ('RF-000009', '2021-08-11 11:25:00', '2021-08-12 07:50:00', '2021-08-14 17:20:00', 'EMP-000004', 'V2304', 'CUS-000009', 1080000),
    ('RF-000010', '2021-09-25 16:10:00', '2021-09-26 12:10:00', '2021-09-28 14:40:00', 'EMP-000005', 'V2408', 'CUS-000014', 1080000),
    ('RF-000011', '2021-10-10 09:00:00', '2021-10-11 10:45:00', '2021-10-13 11:15:00', 'EMP-000001', 'T1101', 'CUS-000013', 480000),
    ('RF-000012', '2021-11-20 08:45:00', '2021-11-21 14:20:00', '2021-11-23 10:00:00', 'EMP-000002', 'T1109', 'CUS-000008', 480000),
    ('RF-000013', '2021-12-02 07:30:00', '2021-12-03 13:10:00', '2021-12-05 12:25:00', 'EMP-000003', 'T1105', 'CUS-000006', 720000),
    ('RF-000014', '2021-12-15 10:25:00', '2021-12-16 09:40:00', '2021-12-18 14:35:00', 'EMP-000004', 'T1203', 'CUS-000017', 720000),
    ('RF-000015', '2021-12-28 15:30:00', '2021-12-29 12:50:00', '2021-12-31 10:30:00', 'EMP-000005', 'T1307', 'CUS-000004', 1440000),
    ('RF-000016', '2022-01-05 11:15:00', '2022-01-06 09:15:00', '2022-01-08 16:00:00', 'EMP-000001', 'V2210', 'CUS-000019', 1440000),
    ('RF-000017', '2022-01-15 08:50:00', '2022-01-16 11:00:00', '2022-01-18 13:30:00', 'EMP-000002', 'V2102', 'CUS-000003', 510000),
    ('RF-000018', '2022-02-10 14:10:00', '2022-02-11 10:20:00', '2022-02-13 09:30:00', 'EMP-000003', 'V2206', 'CUS-000018', 510000),
    ('RF-000019', '2022-02-20 16:30:00', '2022-02-21 08:25:00', '2022-02-23 13:15:00', 'EMP-000004', 'V2304', 'CUS-000021', 1080000),
    ('RF-000020', '2022-03-10 12:05:00', '2022-03-11 11:50:00', '2022-03-14 15:45:00', 'EMP-000005', 'V2408', 'CUS-000020', 1080000),
    ('RF-000021', '2022-04-05 07:30:00', '2022-04-06 13:20:00', '2022-04-09 10:50:00', 'EMP-000001', 'T1101', 'CUS-000017', 480000),
    ('RF-000022', '2022-04-20 10:15:00', '2022-04-21 09:05:00', '2022-04-24 14:35:00', 'EMP-000002', 'T1109', 'CUS-000023', 480000),
    ('RF-000023', '2022-05-15 11:20:00', '2022-05-16 07:50:00', '2022-05-19 16:10:00', 'EMP-000003', 'T1105', 'CUS-000012', 720000),
    ('RF-000024', '2022-06-10 09:40:00', '2022-06-11 10:25:00', '2022-06-13 12:55:00', 'EMP-000004', 'T1203', 'CUS-000009', 720000),
    ('RF-000025', '2022-06-25 08:55:00', '2022-06-26 09:50:00', '2022-06-29 11:40:00', 'EMP-000005', 'T1307', 'CUS-000026', 1440000),
    ('RF-000026', '2022-07-05 13:15:00', '2022-07-06 10:40:00', '2022-07-09 08:50:00', 'EMP-000001', 'V2210', 'CUS-000027', 1440000),
    ('RF-000027', '2022-07-20 16:45:00', '2022-07-21 09:30:00', '2022-07-24 15:25:00', 'EMP-000002', 'V2102', 'CUS-000022', 510000),
    ('RF-000028', '2022-08-05 14:50:00', '2022-08-06 12:45:00', '2022-08-08 13:20:00', 'EMP-000003', 'V2206', 'CUS-000028', 510000),
    ('RF-000029', '2022-08-20 08:30:00', '2022-08-21 11:30:00', '2022-08-23 14:40:00', 'EMP-000004', 'V2304', 'CUS-000010', 1080000),
    ('RF-000030', '2022-09-15 10:35:00', '2022-09-16 10:05:00', '2022-09-18 15:20:00', 'EMP-000005', 'V2408', 'CUS-000005', 1080000),
    ('RF-000031', '2022-10-10 08:25:00', '2022-10-11 14:50:00', '2022-10-13 16:35:00', 'EMP-000001', 'T1101', 'CUS-000019', 480000),
    ('RF-000032', '2022-10-20 10:15:00', '2022-10-21 11:00:00', '2022-10-23 17:20:00', 'EMP-000002', 'T1109', 'CUS-000011', 480000),
    ('RF-000033', '2022-11-05 07:55:00', '2022-11-06 10:30:00', '2022-11-09 13:15:00', 'EMP-000003', 'T1105', 'CUS-000015', 720000),
    ('RF-000034', '2022-11-25 14:20:00', '2022-11-26 12:35:00', '2022-11-29 09:10:00', 'EMP-000004', 'T1203', 'CUS-000006', 720000),
    ('RF-000035', '2022-12-01 11:10:00', '2022-12-02 07:20:00', '2022-12-05 15:00:00', 'EMP-000005', 'T1307', 'CUS-000012', 1440000),
    ('RF-000036', '2022-12-05 09:35:00', '2022-12-06 11:40:00', '2022-12-08 16:15:00', 'EMP-000001', 'V2210', 'CUS-000009', 1440000),
	('RF-000037', '2022-12-10 10:25:00', '2022-12-11 12:50:00', '2022-12-13 13:30:00', 'EMP-000002', 'V2102', 'CUS-000025', 510000),
    ('RF-000038', '2022-12-15 14:45:00', '2022-12-16 08:40:00', '2022-12-18 15:40:00', 'EMP-000003', 'V2206', 'CUS-000017', 510000),
    ('RF-000039', '2022-12-20 08:20:00', '2022-12-21 09:30:00', '2022-12-24 16:25:00', 'EMP-000004', 'V2304', 'CUS-000014', 1080000),
    ('RF-000040', '2022-12-25 13:35:00', '2022-12-26 10:45:00', '2022-12-28 12:10:00', 'EMP-000005', 'V2408', 'CUS-000030', 1080000),
    ('RF-000041', '2023-01-15 09:30:00', '2023-01-16 14:00:00', '2023-01-18 12:00:00', 'EMP-000001', 'T1101', 'CUS-000022', 480000),
    ('RF-000042', '2023-01-22 10:15:00', '2023-01-23 13:00:00', '2023-01-25 15:30:00', 'EMP-000002', 'T1109', 'CUS-000006', 480000),
    ('RF-000043', '2023-02-10 08:45:00', '2023-02-11 12:30:00', '2023-02-13 10:00:00', 'EMP-000003', 'T1105', 'CUS-000028', 720000),
    ('RF-000044', '2023-02-20 14:30:00', '2023-02-21 11:00:00', '2023-02-23 09:30:00', 'EMP-000004', 'T1203', 'CUS-000011', 720000),
    ('RF-000045', '2023-03-05 16:00:00', '2023-03-06 14:15:00', '2023-03-08 12:10:00', 'EMP-000005', 'T1307', 'CUS-000004', 1440000),
    ('RF-000046', '2023-03-15 09:00:00', '2023-03-16 10:20:00', '2023-03-18 15:00:00', 'EMP-000001', 'V2210', 'CUS-000019', 1440000),
    ('RF-000047', '2023-04-01 10:25:00', '2023-04-02 14:30:00', '2023-04-04 11:45:00', 'EMP-000002', 'V2102', 'CUS-000008', 510000),
    ('RF-000048', '2023-04-12 09:15:00', '2023-04-13 10:40:00', '2023-04-15 16:30:00', 'EMP-000003', 'V2206', 'CUS-000013', 510000),
    ('RF-000049', '2023-05-05 11:35:00', '2023-05-06 12:25:00', '2023-05-08 10:45:00', 'EMP-000004', 'V2304', 'CUS-000021', 1080000),
    ('RF-000050', '2023-05-20 13:55:00', '2023-05-21 09:50:00', '2023-05-23 14:25:00', 'EMP-000005', 'V2408', 'CUS-000007', 1080000),
    ('RF-000051', '2023-06-05 15:20:00', '2023-06-06 08:40:00', '2023-06-09 13:15:00', 'EMP-000001', 'T1101', 'CUS-000001', 480000),
    ('RF-000052', '2023-06-15 07:10:00', '2023-06-16 11:30:00', '2023-06-18 12:50:00', 'EMP-000002', 'T1109', 'CUS-000015', 480000),
    ('RF-000053', '2023-07-01 14:25:00', '2023-07-02 10:20:00', '2023-07-04 16:15:00', 'EMP-000003', 'T1105', 'CUS-000012', 720000),
    ('RF-000054', '2023-07-20 13:45:00', '2023-07-21 12:50:00', '2023-07-23 15:40:00', 'EMP-000004', 'T1203', 'CUS-000026', 720000),
    ('RF-000055', '2023-08-05 09:00:00', '2023-08-06 14:30:00', '2023-08-08 11:20:00', 'EMP-000005', 'T1307', 'CUS-000023', 1440000),
    ('RF-000056', '2023-08-15 08:55:00', '2023-08-16 13:45:00', '2023-08-18 12:05:00', 'EMP-000001', 'V2210', 'CUS-000010', 1440000),
    ('RF-000057', '2023-09-10 11:30:00', '2023-09-11 09:25:00', '2023-09-13 16:50:00', 'EMP-000002', 'V2102', 'CUS-000005', 510000),
    ('RF-000058', '2023-09-20 08:40:00', '2023-09-21 15:30:00', '2023-09-24 12:25:00', 'EMP-000003', 'V2206', 'CUS-000020', 510000),
    ('RF-000059', '2023-10-01 16:20:00', '2023-10-02 09:50:00', '2023-10-04 10:00:00', 'EMP-000004', 'V2304', 'CUS-000003', 1080000),
    ('RF-000060', '2023-10-15 12:40:00', '2023-10-16 12:30:00', '2023-10-19 11:30:00', 'EMP-000005', 'V2408', 'CUS-000027', 1080000),
    ('RF-000061', '2023-11-05 09:10:00', '2023-11-06 14:00:00', '2023-11-08 15:20:00', 'EMP-000001', 'T1101', 'CUS-000016', 480000),
    ('RF-000062', '2023-11-15 10:30:00', '2023-11-16 08:45:00', '2023-11-18 14:35:00', 'EMP-000002', 'T1109', 'CUS-000024', 480000),
    ('RF-000063', '2023-11-22 13:50:00', '2023-11-23 09:55:00', '2023-11-25 10:45:00', 'EMP-000003', 'T1105', 'CUS-000018', 720000),
    ('RF-000064', '2023-12-01 15:15:00', '2023-12-02 10:30:00', '2023-12-04 13:10:00', 'EMP-000004', 'T1203', 'CUS-000029', 720000),
    ('RF-000065', '2023-12-05 16:00:00', '2023-12-06 13:20:00', '2023-12-09 11:40:00', 'EMP-000005', 'T1307', 'CUS-000002', 1440000),
    ('RF-000066', '2023-12-10 09:25:00', '2023-12-11 11:45:00', '2023-12-13 12:15:00', 'EMP-000001', 'V2210', 'CUS-000009', 1440000),
    ('RF-000067', '2023-12-15 07:55:00', '2023-12-16 12:00:00', '2023-12-18 16:30:00', 'EMP-000002', 'V2102', 'CUS-000011', 510000),
    ('RF-000068', '2023-12-20 10:15:00', '2023-12-21 10:50:00', '2023-12-23 09:45:00', 'EMP-000003', 'V2206', 'CUS-000020', 510000),
    ('RF-000069', '2023-12-25 08:40:00', '2023-12-26 13:30:00', '2023-12-28 15:20:00', 'EMP-000004', 'V2304', 'CUS-000004', 1080000),
    ('RF-000070', '2023-12-28 11:50:00', '2023-12-29 15:10:00', '2023-12-31 12:55:00', 'EMP-000005', 'V2408', 'CUS-000030', 1080000),
	('RF-000071', '2024-01-05 10:30:00', '2024-01-06 14:00:00', '2024-01-08 11:00:00', 'EMP-000001', 'T1101', 'CUS-000007', 240000),
    ('RF-000072', '2024-01-10 09:15:00', '2024-01-11 13:00:00', '2024-01-14 12:30:00', 'EMP-000002', 'T1109', 'CUS-000019', 240000),
    ('RF-000073', '2024-01-20 11:00:00', '2024-01-21 12:30:00', '2024-01-24 10:45:00', 'EMP-000003', 'T1105', 'CUS-000013', 255000),
    ('RF-000074', '2024-02-02 13:45:00', '2024-02-03 14:15:00', '2024-02-06 11:30:00', 'EMP-000004', 'T1203', 'CUS-000025', 255000),
    ('RF-000075', '2024-02-15 14:30:00', '2024-02-16 11:00:00', '2024-02-18 13:00:00', 'EMP-000005', 'T1307', 'CUS-000002', 480000),
    ('RF-000076', '2024-02-25 08:50:00', '2024-02-26 12:45:00', '2024-02-28 09:20:00', 'EMP-000001', 'V2210', 'CUS-000016', 480000),
    ('RF-000077', '2024-03-03 11:30:00', '2024-03-04 15:00:00', '2024-03-06 14:45:00', 'EMP-000002', 'V2102', 'CUS-000011', 255000),
    ('RF-000078', '2024-03-15 13:15:00', '2024-03-16 11:20:00', '2024-03-19 12:30:00', 'EMP-000003', 'V2206', 'CUS-000021', 540000),
    ('RF-000079', '2024-03-25 10:00:00', '2024-03-26 14:00:00', '2024-03-29 10:30:00', 'EMP-000004', 'V2304', 'CUS-000027', 540000),
    ('RF-000080', '2024-04-05 15:25:00', '2024-04-06 09:40:00', '2024-04-09 16:10:00', 'EMP-000005', 'V2408', 'CUS-000005', 540000),
    ('RF-000081', '2024-04-15 07:45:00', '2024-04-16 11:10:00', '2024-04-19 09:20:00', 'EMP-000001', 'T1101', 'CUS-000028', 240000),
    ('RF-000082', '2024-05-01 10:30:00', '2024-05-02 12:40:00', '2024-05-04 14:00:00', 'EMP-000002', 'T1109', 'CUS-000004', 240000),
    ('RF-000083', '2024-05-10 09:15:00', '2024-05-11 10:20:00', '2024-05-14 13:50:00', 'EMP-000003', 'T1105', 'CUS-000009', 255000),
    ('RF-000084', '2024-05-20 16:00:00', '2024-05-21 08:00:00', '2024-05-24 15:00:00', 'EMP-000004', 'T1203', 'CUS-000012', 255000),
    ('RF-000085', '2024-05-25 11:30:00', '2024-05-26 12:30:00', '2024-05-29 10:00:00', 'EMP-000005', 'T1307', 'CUS-000020', 480000),
    ('RF-000086', '2024-06-01 08:15:00', '2024-06-02 09:50:00', '2024-06-04 13:45:00', 'EMP-000001', 'V2210', 'CUS-000018', 480000),
    ('RF-000087', '2024-06-10 07:50:00', '2024-06-11 13:30:00', '2024-06-14 10:30:00', 'EMP-000002', 'V2102', 'CUS-000014', 255000),
    ('RF-000088', '2024-06-15 12:10:00', '2024-06-16 14:20:00', '2024-06-19 16:00:00', 'EMP-000003', 'V2206', 'CUS-000017', 540000),
    ('RF-000089', '2024-06-25 10:35:00', '2024-06-26 12:10:00', '2024-06-28 14:10:00', 'EMP-000004', 'V2304', 'CUS-000003', 540000),
    ('RF-000090', '2024-07-01 15:15:00', '2024-07-02 11:30:00', '2024-07-04 10:20:00', 'EMP-000005', 'V2408', 'CUS-000006', 540000),
    ('RF-000091', '2024-07-05 08:45:00', '2024-07-06 10:00:00', '2024-07-09 09:00:00', 'EMP-000001', 'T1101', 'CUS-000029', 240000),
    ('RF-000092', '2024-07-10 12:30:00', '2024-07-11 11:15:00', '2024-07-14 13:00:00', 'EMP-000002', 'T1109', 'CUS-000022', 240000),
    ('RF-000093', '2024-07-15 09:10:00', '2024-07-16 12:30:00', '2024-07-19 15:30:00', 'EMP-000003', 'T1105', 'CUS-000010', 255000),
    ('RF-000094', '2024-07-20 07:50:00', '2024-07-21 09:50:00', '2024-07-24 08:30:00', 'EMP-000004', 'T1203', 'CUS-000008', 255000),
    ('RF-000095', '2024-07-25 13:30:00', '2024-07-26 10:50:00', '2024-07-29 14:45:00', 'EMP-000005', 'T1307', 'CUS-000026', 480000),
    ('RF-000096', '2024-08-01 14:15:00', '2024-08-02 10:40:00', '2024-08-04 13:00:00', 'EMP-000001', 'V2210', 'CUS-000015', 480000),
    ('RF-000097', '2024-08-10 09:35:00', '2024-08-11 12:20:00', '2024-08-14 16:00:00', 'EMP-000002', 'V2102', 'CUS-000001', 255000),
    ('RF-000098', '2024-08-15 11:50:00', '2024-08-16 10:10:00', '2024-08-19 15:20:00', 'EMP-000003', 'V2206', 'CUS-000024', 540000),
    ('RF-000099', '2024-08-25 12:45:00', '2024-08-26 15:30:00', '2024-08-28 13:15:00', 'EMP-000004', 'V2304', 'CUS-000023', 540000),
    ('RF-000100', '2024-09-01 10:20:00', '2024-09-02 13:10:00', '2024-09-04 15:00:00', 'EMP-000005', 'V2408', 'CUS-000006', 540000);
GO

-- Thêm dữ liệu vào bảng Invoice
INSERT INTO Invoice (invoiceID, invoiceDate, roomCharge, servicesCharge, reservationFormID)
VALUES
    ('INV-000006', '2021-05-25 10:10:10.897', 10000, 5000, 'RF-000006'),
    ('INV-000007', '2021-06-18 15:30:30.897', 10000, 5000, 'RF-000007'),
    ('INV-000008', '2021-07-08 08:20:25.897', 10000, 5000, 'RF-000008'),
    ('INV-000009', '2021-08-14 18:55:45.897', 10000, 5000, 'RF-000009'),
    ('INV-000010', '2021-09-28 12:05:55.897', 10000, 5000, 'RF-000010'),
    ('INV-000011', '2021-10-13 17:45:30.897', 10000, 5000, 'RF-000011'),
    ('INV-000012', '2021-11-23 09:15:50.897', 10000, 5000, 'RF-000012'),
    ('INV-000013', '2021-12-05 14:25:20.897', 10000, 5000, 'RF-000013'),
    ('INV-000014', '2021-12-18 08:45:35.897', 10000, 5000, 'RF-000014'),
    ('INV-000015', '2021-12-31 19:20:10.897', 10000, 5000, 'RF-000015'),
    ('INV-000016', '2022-01-08 10:30:45.897', 10000, 5000, 'RF-000016'),
    ('INV-000017', '2022-01-18 16:55:25.897', 10000, 5000, 'RF-000017'),
    ('INV-000018', '2022-02-13 09:40:15.897', 10000, 5000, 'RF-000018'),
    ('INV-000019', '2022-02-23 11:25:55.897', 10000, 5000, 'RF-000019'),
    ('INV-000020', '2022-03-14 14:10:35.897', 10000, 5000, 'RF-000020'),
    ('INV-000021', '2022-04-09 13:50:40.897', 10000, 5000, 'RF-000021'),
    ('INV-000022', '2022-04-24 08:15:25.897', 10000, 5000, 'RF-000022'),
    ('INV-000023', '2022-05-19 18:30:15.897', 10000, 5000, 'RF-000023'),
    ('INV-000024', '2022-06-13 10:05:55.897', 10000, 5000, 'RF-000024'),
    ('INV-000025', '2022-06-29 17:25:10.897', 10000, 5000, 'RF-000025'),
    ('INV-000026', '2022-07-09 09:45:50.897', 10000, 5000, 'RF-000026'),
    ('INV-000027', '2022-07-24 14:10:20.897', 10000, 5000, 'RF-000027'),
    ('INV-000028', '2022-08-08 11:35:35.897', 10000, 5000, 'RF-000028'),
    ('INV-000029', '2022-08-23 12:55:05.897', 10000, 5000, 'RF-000029'),
    ('INV-000030', '2022-09-18 08:50:30.897', 10000, 5000, 'RF-000030'),
    ('INV-000031', '2022-10-13 14:15:30.897', 10000, 5000, 'RF-000031'),
    ('INV-000032', '2022-10-23 09:25:45.897', 10000, 5000, 'RF-000032'),
    ('INV-000033', '2022-11-09 16:35:20.897', 10000, 5000, 'RF-000033'),
    ('INV-000034', '2022-11-29 08:50:15.897', 10000, 5000, 'RF-000034'),
    ('INV-000035', '2022-12-05 12:45:55.897', 10000, 5000, 'RF-000035'),
    ('INV-000036', '2022-12-08 14:05:10.897', 12000, 6000, 'RF-000036'),
    ('INV-000037', '2022-12-13 10:15:25.897', 12500, 6500, 'RF-000037'),
    ('INV-000038', '2022-12-18 13:25:40.897', 13000, 7000, 'RF-000038'),
    ('INV-000039', '2022-12-24 18:35:55.897', 13500, 7200, 'RF-000039'),
    ('INV-000040', '2022-12-28 11:45:30.897', 14000, 7500, 'RF-000040'),
    ('INV-000041', '2023-01-18 09:10:15.897', 14500, 7800, 'RF-000041'),
    ('INV-000042', '2023-01-25 15:25:45.897', 15000, 8000, 'RF-000042'),
    ('INV-000043', '2023-02-13 08:40:30.897', 15500, 8300, 'RF-000043'),
    ('INV-000044', '2023-02-23 14:55:55.897', 16000, 8500, 'RF-000044'),
    ('INV-000045', '2023-03-08 10:30:20.897', 16500, 9000, 'RF-000045'),
    ('INV-000046', '2023-03-18 13:45:10.897', 17000, 9200, 'RF-000046'),
    ('INV-000047', '2023-04-04 16:05:25.897', 17500, 9500, 'RF-000047'),
    ('INV-000048', '2023-04-15 08:15:40.897', 18000, 9700, 'RF-000048'),
    ('INV-000049', '2023-05-08 17:35:50.897', 18500, 10000, 'RF-000049'),
    ('INV-000050', '2023-05-23 10:25:30.897', 19000, 10500, 'RF-000050'),
    ('INV-000051', '2023-06-09 11:50:45.897', 19500, 11000, 'RF-000051'),
    ('INV-000052', '2023-06-18 15:15:10.897', 20000, 11200, 'RF-000052'),
    ('INV-000053', '2023-07-04 14:30:55.897', 20500, 11500, 'RF-000053'),
    ('INV-000054', '2023-07-23 08:45:20.897', 21000, 12000, 'RF-000054'),
    ('INV-000055', '2023-08-08 13:05:35.897', 21500, 12500, 'RF-000055'),
    ('INV-000056', '2023-08-18 18:20:15.897', 22000, 13000, 'RF-000056'),
    ('INV-000057', '2023-09-13 12:35:30.897', 22500, 13500, 'RF-000057'),
    ('INV-000058', '2023-09-24 09:45:55.897', 23000, 14000, 'RF-000058'),
    ('INV-000059', '2023-10-04 16:15:25.897', 23500, 14500, 'RF-000059'),
    ('INV-000060', '2023-10-19 14:50:10.897', 24000, 15000, 'RF-000060'),
    ('INV-000061', '2023-11-08 09:00:15.897', 24500, 15500, 'RF-000061'),
    ('INV-000062', '2023-11-18 11:15:30.897', 25000, 16000, 'RF-000062'),
    ('INV-000063', '2023-11-25 14:30:45.897', 25500, 16500, 'RF-000063'),
    ('INV-000064', '2023-12-04 09:45:10.897', 26000, 17000, 'RF-000064'),
    ('INV-000065', '2023-12-09 13:00:25.897', 26500, 17500, 'RF-000065'),
    ('INV-000066', '2023-12-13 16:15:40.897', 27000, 18000, 'RF-000066'),
    ('INV-000067', '2023-12-18 10:30:55.897', 27500, 18500, 'RF-000067'),
    ('INV-000068', '2023-12-23 15:45:15.897', 28000, 19000, 'RF-000068'),
    ('INV-000069', '2023-12-28 17:00:30.897', 28500, 19500, 'RF-000069'),
    ('INV-000070', '2023-12-31 13:15:45.897', 29000, 20000, 'RF-000070'),
    ('INV-000071', '2024-01-05 09:30:10.897', 10000, 5000, 'RF-000071'),
    ('INV-000072', '2024-01-10 12:45:25.897', 10000, 5000, 'RF-000072'),
    ('INV-000073', '2024-01-20 15:00:40.897', 10000, 5000, 'RF-000073'),
    ('INV-000074', '2024-02-02 11:15:55.897', 10000, 5000, 'RF-000074'),
    ('INV-000075', '2024-02-15 14:30:20.897', 10000, 5000, 'RF-000075'),
    ('INV-000076', '2024-02-25 08:45:35.897', 10000, 5000, 'RF-000076'),
    ('INV-000077', '2024-03-03 10:00:50.897', 10000, 5000, 'RF-000077'),
    ('INV-000078', '2024-03-15 13:15:05.897', 10000, 5000, 'RF-000078'),
    ('INV-000079', '2024-03-25 16:30:20.897', 10000, 5000, 'RF-000079'),
    ('INV-000080', '2024-04-05 09:45:35.897', 10000, 5000, 'RF-000080'),
    ('INV-000081', '2024-04-15 14:00:50.897', 10000, 5000, 'RF-000081'),
    ('INV-000082', '2024-05-01 10:15:10.897', 10000, 5000, 'RF-000082'),
    ('INV-000083', '2024-05-10 13:30:25.897', 10000, 5000, 'RF-000083'),
    ('INV-000084', '2024-05-20 16:45:40.897', 10000, 5000, 'RF-000084'),
    ('INV-000085', '2024-06-05 09:00:55.897', 10000, 5000, 'RF-000085'),
    ('INV-000086', '2024-06-15 11:15:10.897', 10000, 5000, 'RF-000086'),
    ('INV-000087', '2024-06-25 15:30:25.897', 10000, 5000, 'RF-000087'),
    ('INV-000088', '2024-07-01 12:45:40.897', 10000, 5000, 'RF-000088'),
    ('INV-000089', '2024-07-10 09:00:55.897', 10000, 5000, 'RF-000089'),
    ('INV-000090', '2024-07-20 10:15:10.897', 10000, 5000, 'RF-000090'),
    ('INV-000091', '2024-08-05 09:30:15.897', 10000, 5000, 'RF-000091'),
    ('INV-000092', '2024-08-15 11:45:30.897', 10000, 5000, 'RF-000092'),
    ('INV-000093', '2024-08-25 14:00:45.897', 10000, 5000, 'RF-000093'),
    ('INV-000094', '2024-09-05 09:15:10.897', 10000, 5000, 'RF-000094'),
    ('INV-000095', '2024-09-15 13:30:25.897', 10000, 5000, 'RF-000095'),
    ('INV-000096', '2024-09-25 16:45:40.897', 10000, 5000, 'RF-000096'),
    ('INV-000097', '2024-10-01 10:00:55.897', 10000, 5000, 'RF-000097'),
    ('INV-000098', '2024-10-10 12:15:10.897', 10000, 5000, 'RF-000098'),
    ('INV-000099', '2024-10-20 15:30:25.897', 10000, 5000, 'RF-000099'),
    ('INV-000100', '2024-11-01 09:45:40.897', 10000, 5000, 'RF-000100');
GO


--Phiếu 1: đã đặt phòng, có thể checkin nhưng chưa vào
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000107', GETDATE(), GETDATE(), DATEADD(DAY, 3, GETDATE()), 'EMP-000001', 'T1101', 'CUS-000001', 500000, 'ACTIVATE');
GO

--Phiếu 2: đã checkin, có 1 bảng ghi trong HistoryCheckIn,
-- 1 bảng ghi RomReservationDetail và cập trạng thái phòng
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000108', DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, 3, GETDATE()), 'EMP-000002', 'T1105', 'CUS-000002', 500000, 'ACTIVATE');

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
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000109', DATEADD(DAY, -5, GETDATE()), DATEADD(DAY, -4, GETDATE()), DATEADD(HOUR, -1, GETDATE()), 'EMP-000003', 'V2102', 'CUS-000003', 500000, 'ACTIVATE');

INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
VALUES ('HCI-000002', DATEADD(DAY, -4, GETDATE()), 'RF-000109', 'EMP-000003');

INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
VALUES
    ('RRD-000002', DATEADD(DAY, -4, GETDATE()), 'V2102', 'RF-000109', 'EMP-000003');

UPDATE Room
SET roomStatus = 'OVERDUE'
WHERE roomID = 'V2102';
GO

INSERT INTO RoomUsageService (roomUsageServiceID, reservationFormID, hotelServiceId, quantity, unitPrice, dateAdded, employeeID)
VALUES
    ('RUS-000001', 'RF-000109', 'HS-000001', 2, 100000, GETDATE(), 'EMP-000003'),
    ('RUS-000002', 'RF-000109', 'HS-000002', 1, 200000, GETDATE(), 'EMP-000003'),
    ('RUS-000003', 'RF-000109', 'HS-000003', 3, 150000, GETDATE(), 'EMP-000003'),
    ('RUS-000004', 'RF-000109', 'HS-000004', 1, 50000, GETDATE(), 'EMP-000003');
GO

-- Phiếu 4: đã checkin và gần tới giờ checkout (trong 5 phút nữa)
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000110', DATEADD(DAY, -1, GETDATE()), DATEADD(HOUR, -23, GETDATE()), DATEADD(MINUTE, 2, GETDATE()), 'EMP-000004', 'V2206', 'CUS-000004', 500000, 'ACTIVATE');

INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
VALUES ('HCI-000003', DATEADD(HOUR, -23, GETDATE()), 'RF-000110', 'EMP-000004');

INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
VALUES
    ('RRD-000003', DATEADD(HOUR, -23, GETDATE()), 'V2206', 'RF-000110', 'EMP-000004');

-- Cập nhật trạng thái phòng
UPDATE Room
SET roomStatus = 'ON_USE'
WHERE roomID = 'V2206';
GO

-- Phiếu 5: Check-in đã được thực hiện, gần quá 2 tiếng thời gian checkout (còn 3 phút nữa để quá 2 tiếng)
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000111', DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -1, GETDATE()), DATEADD(HOUR, -2, DATEADD(MINUTE, 1, GETDATE())), 'EMP-000005', 'V2304', 'CUS-000005', 600000, 'ACTIVATE');

-- Thêm thông tin vào HistoryCheckin
INSERT INTO HistoryCheckin (historyCheckInID, checkInDate, reservationFormID, employeeID)
VALUES ('HCI-000004', DATEADD(DAY, -1, GETDATE()), 'RF-000111', 'EMP-000005');

-- Thêm vào RoomReservationDetail
INSERT INTO RoomReservationDetail (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
VALUES ('RRD-000004', DATEADD(DAY, -1, GETDATE()), 'V2304', 'RF-000111', 'EMP-000005');

-- Cập nhật trạng thái phòng
UPDATE Room
SET roomStatus = 'ON_USE'
WHERE roomID = 'V2304';
GO

-- Phiếu 6: Phiếu đặt phòng chưa check-in, với thời gian gần quá 2 tiếng (còn 3 phút nữa)
INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
VALUES ('RF-000112', DATEADD(HOUR, -2, DATEADD(MINUTE, 3, GETDATE())), DATEADD(HOUR, -2, DATEADD(MINUTE, 3, GETDATE())), DATEADD(DAY, 1, GETDATE()), 'EMP-000001', 'T1203', 'CUS-000010', 700000, 'ACTIVATE');
GO

USE master
GO