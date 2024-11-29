const questionsData = {
  "Nhân viên": [
    {
      question: "Làm thế nào để thêm nhân viên?",
      answer: [
        {
          type: "text",
          content: "**Bạn phải có chức vụ quản lý",
        },
        {
          type: "text",
          content: "1. Nhập đầy đủ thông tin nhân viên vào ô bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt1.png",
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Lưu ý:</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Tên nhân viên không rỗng, từ 3 ký tự và đến 30 ký tự.</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Số điện thoại không rỗng, phải có 10 ký tự và ký tự đầu là ký số 0. Đều là ký số.</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Ngày sinh phải cách ngày hiện tại từ 18 năm trở lên</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Thư tín điện tử phải theo mẫu X@Y. X ít nhất 4 ký tự và không quá 30 ký tự không chứa các ký tự đặc biệt như !, #, $, %, ^, &, *, (, ), +, =, {}, [], :, ;, \', ", <, >, ?. Y phải là gmail.com hoặc yahoo.com</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">CCCD phải theo mẫu: XXXYZZTTTTTT. XXXX: Mã thành phố từ 001 đến 096. Y: mã kỷ sinh. Thế kỷ 20 (từ năm 1900 đến hết năm 1999): Nam 0, nữ 1;. Thế kỷ 21 (từ năm 2000 đến hết năm 2099): Nam 2, nữ 3;. ZZ: 2 số cuối năm sinh. TTTTTT: 6 ký số</span><br>',
        },
        {
          type: "text",
          content: "2. Chọn thêm",
        },
        {
          type: "text",
          content:
          '<span style="color: green; font-style: italic;">3. Hiển thị thông báo và thông tin nhân viên vào bảng thông tin là bạn đã thêm thành công</span>'
        },
        {
          type: "image",
          content: "../imgs/pt2.png",
        }
      ],
    },
    {
      question: "Cập nhật thông tin nhân viên?",
      answer: [
        {
          type: "text",
          content: "**Bạn phải có chức vụ quản lý",
        },
        {
          type: "text",
          content: "1. Tìm kiếm nhân viên trên hỗ trợ tìm kiếm hoặc chọn nhân viên trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt3.png",
        },
        {
          type: "text",
          content: "2. Chọn cập nhật, thông tin sẽ hiển thị phía trên, bạn tiến hành thay đổi thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt4.png",
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Lưu ý:</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Tên nhân viên không rỗng, từ 3 ký tự và đến 30 ký tự.</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Số điện thoại không rỗng, phải có 10 ký tự và ký tự đầu là ký số 0. Đều là ký số.</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Ngày sinh phải cách ngày hiện tại từ 18 năm trở lên</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Thư tín điện tử phải theo mẫu X@Y. X ít nhất 4 ký tự và không quá 30 ký tự không chứa các ký tự đặc biệt như !, #, $, %, ^, &, *, (, ), +, =, {}, [], :, ;, \', ", <, >, ?. Y phải là gmail.com hoặc yahoo.com</span><br>',
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">CCCD phải theo mẫu: XXXYZZTTTTTT. XXXX: Mã thành phố từ 001 đến 096. Y: mã kỷ sinh. Thế kỷ 20 (từ năm 1900 đến hết năm 1999): Nam 0, nữ 1;. Thế kỷ 21 (từ năm 2000 đến hết năm 2099): Nam 2, nữ 3;. ZZ: 2 số cuối năm sinh. TTTTTT: 6 ký số</span><br>',
        },
        {
          type: "text",
          content: "3. Chọn cập nhật, nhận thông báo cập nhật thành công",
        },
        {
          type: "text",
          content: "4. Hoặc bạn có thể tự đổi thông tin của bản thân bằng cách chọn vào avata, chọn thay đổi thông tin, rồi tiến hành thay đổi thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt8.png",
        },
        {
          type: "image",
          content: "../imgs/pt9.png",
        },
        {
          type: "image",
          content: "../imgs/pt11.png",
        }
      ],
    },
    {
      question: "Tìm kiếm nhân viên?",
      answer: [
        {
          type: "text",
          content: "**Bạn phải có chức vụ quản lý",
        },
        {
          type: "text",
          content: "1. Nhập thông tin tìm kiếm cần vào các ô, sau đó tìm kiếm, thông tin bạn cần sẽ hiển thị vào bảng thông tin bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt5.png",
        },
        {
          type: "text",
          content:
            '<span style="color: red; font-style: italic;">Lưu ý: Bạn có thể chọn THÔNG TIN để xem thông tin chi tiết hoặc nhấn chuột phải vào hàng thông tin để tiến hành chỉnh sửa thông tin nhân viên</span><br>',
        },
        {
          type: "image",
          content: "../imgs/pt6.png",
        }
      ],
    },
    {
      question: "Tài khoản nhân viên?",
      answer: [
        {
          type: "text",
          content: "**Bạn phải có chức vụ quản lý",
        },
        {
          type: "text",
          content: "Tài khoản sẽ được tạo cùng lúc khi tạo nhân viên, với username mặc định là Mã số nhân viên, và mật khẩu mặc định là test123@",
        },
        {
          type: "text",
          content: "Nếu bạn muốn đổi mật khẩu có 3 cách",
        },
        {
          type: "text",
          content: "Cách 1: Bạn nhờ quản lý đổi mật khẩu (Quản lý chỉ đổi được mật khẩu cho lễ tân, không đổi được mật khẩu cho quản lý khác)",
        },
        {
          type: "image",
          content: "../imgs/pt7.png",
        },
        {
          type: "text",
          content: "Cách 2: Bạn nhập vào ứng dụng, chọn thông tin trên avata, chọn đổi mật khẩu",
        },
        {
          type: "image",
          content: "../imgs/pt8.png",
        },
        {
          type: "image",
          content: "../imgs/pt9.png",
        },
        {
          type: "image",
          content: "../imgs/pt10.png",
        }
      ]
    },
    {
      question: "Phân công ca làm cho nhân viên?",
      answer: [
        {
          type: "text",
          content: "Bạn phải có chức vụ quản lý",
        },
        {
          type: "text",
          content: "**Bạn phải có chức vụ quản lí",
        },
        {
          type: "text",
          content: "1. Bạn tạo ca làm bằng cách nhập thông tin vào các ô thông tin rồi tạo ca làm",
        },
        {
          type: "image",
          content: "../imgs/pt12.png",
        },
        {
          type: "text",
          content: "2. Bạn tiến hành phân công ca bằng các chức năng dưới bảng thông tin, bạn cũng có thể kích chuột 2 lần để xem thông tin chi tiết của ca làm",
        },
        {
          type: "image",
          content: "../imgs/pt13.png",
        },
        {
          type: "image",
          content: "../imgs/pt14.png",
        }
      ]
    }
  ],
  Phòng: [
    {
      question: "Cách đặt phòng?",
      answer: [
        {
          type: "text",
          content: "1. Chọn phòng để tạo phiếu đặt phòng",
        },
        {
          type: "image",
          content: "../imgs/pt15.png",
        },
        {
          type: "text",
          content: "2. Nhập thông tin hợp lệ bên dưới, rồi chọn tạo phiếu đặt phòng",
        },
        {
          type: "image",
          content: "../imgs/pt16.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể xem lại phiếu đặt phòng của phòng khi chọn xem phiếu đặt phòng",
        },
        {
          type: "image",
          content: "../imgs/pt17.png",
        }
      ]
    },
    {
      question: "Gọi dịch vụ?",
      answer:[
        {
          type: "text",
          content: "1. Chọn phòng đang sử dụng để gọi dịch vụ",
        },
        {
          type: "image",
          content: "../imgs/pt15.png",
        },
        {
          type: "text",
          content: "2. Chọn gọi dịch vụ",
        },
        {
          type: "image",
          content: "../imgs/pt18.png",
        },
        {
          type: "text",
          content: "3. Chọn dịch vụ khách hàng cần",
        },
        {
          type: "image",
          content: "../imgs/pt20.png",
        },
      ]
    },
    {
      question: "Nhận phòng?",
      answer: [
        {
          type: "text",
          content: "1. Chọn phòng nhận",
        },
        {
          type: "image",
          content: "../imgs/pt15.png",
        },
        {
          type: "text",
          content: "2. Chọn xem phiếu đặt phòng, sau đó chọn phiếu đặt phòng",
        },
        {
          type: "image",
          content: "../imgs/pt17.png",
        },
        {
          type: "text",
          content: "3. Chọn checkin phòng",
        },
        {
          type: "image",
          content: "../imgs/pt19.png",
        },
      ],
    },
    {
      question: "Chuyển phòng?",
      answer: [
        {
          type: "text",
          content: "1. Chọn phòng đang sử dụng để chuyển phòng",
        },
        {
          type: "image",
          content: "../imgs/pt15.png",
        },
        {
          type: "text",
          content: "2. Chọn chuyển phòng",
        },
        {
          type: "image",
          content: "../imgs/pt18.png",
        },
        {
          type: "text",
          content: "3. Chọn phòng để chuyển phòng",
        },
        {
          type: "image",
          content: "../imgs/pt21.png",
        },
      ],
    },
    {
      question: "Trả phòng",
      answer: [

      ],
    },
    {
      question: "Quản lý phòng?",
      answer: [
        {
          type: "text",
          content: "1. Vào mục quản lý phòng",
        },
        {
          type: "image",
          content: "../imgs/pt22.png",
        },
        {
          type: "text",
          content: "2. Bạn có thể thêm phòng bằng cách nhập thông tin bên dưới và nhấn thêm",
        },
        {
          type: "image",
          content: "../imgs/pt23.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể cập nhật thông tin phòng bằng cách chọn các chức năng bên dưới bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt24.png",
        },
      ],
    },
    {
      question: "Tìm kiếm phòng",
      answer: [
        {
          type: "text",
          content: "1. Vào mục tìm kiếm phòng",
        },
        {
          type: "image",
          content: "../imgs/pt25.png",
        },
        {
          type: "text",
          content: "2. Bạn nhập thông tin bạn có để tìm kiếm bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt23.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể kichs chuột phải để chỉnh sửa thông tin phòng, hoặc tạo phiếu đặt phòng cho phòng này",
        },
        {
          type: "image",
          content: "../imgs/pt27.png",
        },
        {
          type: "text",
          content: "Chỉnh sửa",
        },
        {
          type: "image",
          content: "../imgs/pt28.png",
        },
        {
          type: "text",
          content: "Đặt phòng",
        },
        {
          type: "image",
          content: "../imgs/pt29.png",
        },
      ],
    },
  ],
  "Hóa đơn": [
    {
      question: "Xem chi tiết hóa đơn, xuất hóa đơn sang PDF?",
      answer: [
        {
          type: "text",
          content: "1. Vào mục Hóa đơn",
        },
        {
          type: "image",
          content: "../imgs/pt30.png",
        },
        {
          type: "text",
          content: "2. chọn hóa đơn để xem thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt31.png",
        },
        {
          type: "image",
          content: "../imgs/pt32.png",
        },
        {
          type: "text",
          content: "3. Sau đó bạn chọn xuất PDF để lưu hóa đơn",
        },
        {
          type: "image",
          content: "../imgs/pt33.png",
        },
      ],
    },
  ],
  "Dịch vụ": [
    {
      question: "Thêm loại dịch vụ mới?",
      answer: [
        {
          type: "text",
          content: "1. Thêm thông tin vào các ô bên dưới và nhấn thêm",
        },
        {
          type: "image",
          content: "../imgs/pt34.png",
        },
        {
          type: "text",
          content: "2. Hiện thông báo thêm thành công và thông tin được thêm thêm vào bảng thông tin bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt35.png",
        },
      ],
    },
    {
      question: "Xóa loại dịch vụ?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm loại dịch vụ hoặc chọn loại dịch vụ trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt35.png",
        },
        {
          type: "text",
          content: "2. Chọn xóa, và nhấn đồng ý",
        },
        {
          type: "image",
          content: "../imgs/pt36.png",
        },
      ],
    },
    {
      question: "Chỉnh sửa thông tin loại dịch vụ?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm loại dịch vụ hoặc chọn loại dịch vụ trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt35.png",
        },
        {
          type: "text",
          content: "2. Chọn cập nhật, thông tin sẽ hiện lên trên ô phía trên, bạn tiến hành thay đổi thông tin và nhấn cập nhật",
        },
        {
          type: "image",
          content: "../imgs/pt37.png",
        },
      ],
    },
    {
      question: "Thêm dịch vụ mới?",
      answer: [
        {
          type: "text",
          content: "1. Thêm thông tin vào các ô bên dưới và nhấn thêm",
        },
        {
          type: "image",
          content: "../imgs/pt38.png",
        },
        {
          type: "text",
          content: "2. Hiện thông báo thêm thành công và thông tin được thêm thêm vào bảng thông tin bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt39.png",
        },
      ],
    },
    {
      question: "Xóa dịch vụ?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm dịch vụ hoặc chọn dịch vụ trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt39.png",
        },
        {
          type: "text",
          content: "2. Chọn xóa, và nhấn đồng ý",
        },
        {
          type: "image",
          content: "../imgs/pt40.png",
        },
      ],
    },
    {
      question: "Chỉnh sửa thông tin dịch vụ?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm dịch vụ hoặc chọn loại dịch vụ trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt39.png",
        },
        {
          type: "text",
          content: "2. Chọn cập nhật, thông tin sẽ hiện lên trên ô phía trên, bạn tiến hành thay đổi thông tin và nhấn cập nhật",
        },
        {
          type: "image",
          content: "../imgs/pt41.png",
        },
      ],
    },
    {
      question: "Tìm kiếm dịch vụ?",
      answer: [
        {
          type: "text",
          content: "1. Bạn nhập thông tin hiện có để tìm kiếm",
        },
        {
          type: "image",
          content: "../imgs/pt42.png",
        },
        {
          type: "text",
          content: "2. Thông tin sẽ hiển thị dưới bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt43.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể nhấn chuột phải để chỉnh sửa thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt44.png",
        },
      ],
    },
  ],
  "Khách hàng": [
    {
      question: "Thêm khách hàng mới?",
      answer: [
        {
          type: "text",
          content: "1. Thêm thông tin vào các ô bên dưới và nhấn thêm",
        },
        {
          type: "image",
          content: "../imgs/pt45.png",
        },
        {
          type: "text",
          content: "2. Hiện thông báo thêm thành công và thông tin được thêm thêm vào bảng thông tin bên dưới",
        },
        {
          type: "image",
          content: "../imgs/pt46.png",
        },
      ],
    },
    {
      question: "Xóa khách hàng?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm khách hàng hoặc chọn khách hàng trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt46.png",
        },
        {
          type: "text",
          content: "2. Chọn xóa, và nhấn đồng ý",
        },
        {
          type: "image",
          content: "../imgs/pt47.png",
        },
      ],
    },
    {
      question: "Chỉnh sửa thông tin khách hàng?",
      answer: [
        {
          type: "text",
          content: "1. Bạn tìm kiếm khách hàng hoặc chọn khách hàng trên bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt46.png",
        },
        {
          type: "text",
          content: "2. Chọn cập nhật, thông tin sẽ hiện lên trên ô phía trên, bạn tiến hành thay đổi thông tin và nhấn cập nhật",
        },
        {
          type: "image",
          content: "../imgs/pt48.png",
        },
      ],
    },
    {
      question: "Tìm kiếm khách hàng?",
      answer: [
        {
          type: "text",
          content: "1. Bạn nhập thông tin hiện có để tìm kiếm",
        },
        {
          type: "image",
          content: "../imgs/pt49.png",
        },
        {
          type: "text",
          content: "2. Thông tin sẽ hiển thị dưới bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt50.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể nhấn chuột phải để chỉnh sửa thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt51.png",
        },
      ],
    },
  ],
  "Thống kê": [
    {
      question: "Xem thống kê tổng doanh thu?",
      answer: [
        {
          type: "text",
          content: "1. Chọn các lựa chọn để thống kê doanh thu",
        },
        {
          type: "image",
          content: "../imgs/pt52.png",
        },
        {
          type: "text",
          content: "2. Sau khi chọn xong thông tin sẽ hiện biên dưới bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt53.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể chọn biểu đồ để xem dưới dạng biểu đồ",
        },
        {
          type: "image",
          content: "../imgs/pt54.png",
        },
      ],
    },
    {
      question: "Xem thống kê tỉ lệ sử dụng phòng?",
      answer: [
        {
          type: "text",
          content: "1. Chọn các lựa chọn để thống kê doanh thu",
        },
        {
          type: "image",
          content: "../imgs/pt55.png",
        },
        {
          type: "text",
          content: "2. Sau khi chọn xong thông tin sẽ hiện biên dưới bảng thông tin",
        },
        {
          type: "image",
          content: "../imgs/pt56.png",
        },
        {
          type: "text",
          content: "3. Bạn có thể chọn biểu đồ để xem dưới dạng biểu đồ",
        },
        {
          type: "image",
          content: "../imgs/pt57.png",
        },
      ],
    },
    {
      question: "Báo cáo thống kê(Xuất sang file Excel)?",
      answer: [
        {
          type: "text",
          content: "1. Chọn xuất excel",
        },
        {
          type: "image",
          content: "../imgs/pt55.png",
        },
        {
          type: "text",
          content: "2. Chọn nơi lưu trữ",
        },
        {
          type: "image",
          content: "../imgs/pt58.png",
        },
        {
          type: "text",
          content: "3. Bấm lưu",
        },
        {
          type: "image",
          content: "../imgs/pt59.png",
        },
      ],
    },
  ],
  "Thiết lập": [
    {
      question: "Làm sao để sao lưu dữ liệu?",
      answer: [
        {
          type: "text",
          content: "1. Chọn các lựa chọn để sao lưu dữ liệu",
        },
        {
          type: "image",
          content: "../imgs/pt60.png",
        },
        {
          type: "text",
          content: "2. Chọn đồng ý để sao lưu, vào thư mục kiểm tra",
        },
        {
          type: "image",
          content: "../imgs/pt61.png",
        },
      ],
    },
    {
      question: "Làm thế nào để phục hồi dữ liệu?",
      answer: [
        {
          type: "text",
          content: "1. Chọn thư mục phục hồi",
        },
        {
          type: "image",
          content: "../imgs/pt62.png",
        },
        {
          type: "text",
          content: "2. Chọn thư mục, sau đó phục hồi",
        },
      ],
    },
  
  ],
};

