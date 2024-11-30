-- Tạo cơ sở dữ liệu HotelDatabase
USE master;
GO
CREATE DATABASE HotelTestDatabase;
GO

-- Sử dụng cơ sở dữ liệu HotelDatabase
USE HotelTestDatabase;
GO

-- Xóa cơ sở dữ liệu
--USE master
--DROP DATABASE HotelTestDatabase

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
