// ==================================================================================================================
// 1. Dữ liệu
// ==================================================================================================================
const features = [
  {
    name: "Nhân viên",
    keywords: ["nhân viên", "nhan vien", "employee", "staff", "nv"],
    link: "/employee-management.html",
    img: "../imgs/menu_icon/ic_employee.png",
  },
  {
    name: "Phòng",
    keywords: ["phòng", "phong", "room", "room management", "rm"],
    link: "/room-management.html",
    img: "../imgs/menu_icon/ic_room.png",
  },
  {
    name: "Hóa đơn",
    keywords: ["hóa đơn", "hoa don", "invoice", "bill", "hd"],
    link: "/invoice-management.html",
    img: "../imgs/menu_icon/ic_invoice.png",
  },
  {
    name: "Dịch vụ",
    keywords: ["dịch vụ", "dich vu", "service", "hotel service", "dv"],
    link: "/service-management.html",
    img: "../imgs/menu_icon/ic_service.png",
  },
  {
    name: "Khách hàng",
    keywords: ["khách hàng", "khach hang", "customer", "client", "kh"],
    link: "/customer-management.html",
    img: "../imgs/menu_icon/ic_customer.png",
  },
  {
    name: "Thống kê",
    keywords: ["thống kê", "thong ke", "statistics", "analytics", "tk"],
    link: "/statistics.html",
    img: "../imgs/menu_icon/ic_statistics.png",
  },
  {
    name: "Thiết lập",
    keywords: ["thiết lập", "thiet lap", "settings", "configuration", "tl"],
    link: "/settings.html",
    img: "../imgs/menu_icon/ic_settings.png",
  },
];

// ==================================================================================================================
// 2. Lắng nghe sự kiện người dùng nhập. Gọi sự kiện hiển thị kết quả tìm kiếm
// ==================================================================================================================
const inputTextField = document.querySelector(".form-control");
const searchResults = document.getElementById("search-results");

// Xử lý tìm kiếm khi người dùng nhập
inputTextField.addEventListener("input", (event) => {
  const query = event.target.value.toLowerCase();

  // Nếu không có từ khóa, hiển thị toàn bộ danh sách
  const filteredFeatures =
    query.trim() === ""
      ? features
      : features.filter((feature) => {
          return (
            feature.name.toLowerCase().includes(query) ||
            feature.keywords.some((keyword) => keyword.includes(query))
          );
        });

  displayFeatures(filteredFeatures);
});

// ==================================================================================================================
// 3. Hàm hiển thị danh sách chức năng
// ==================================================================================================================
const displayFeatures = (featuresToShow) => {
  searchResults.innerHTML = ""; // Xóa kết quả cũ

  featuresToShow.forEach((feature) => {
    const featureCard = document.createElement("div");
    featureCard.className = "col-md-4";

    featureCard.innerHTML = `
      <div class="card h-100" style="cursor: pointer;">
        <img src="${feature.img}" class="card-img-top card-img-custom" alt="${feature.name}" />
        <div class="card-body text-center">
          <h5 class="card-title">${feature.name}</h5>
        </div>
      </div>
    `;

    // Gắn sự kiện click vào card
    featureCard.addEventListener("click", () => {
      window.location.href = `./details.html?name=${encodeURIComponent(
        feature.name
      )}`;
    });

    searchResults.appendChild(featureCard);
  });
};

// ==================================================================================================================
// 4. Khác
// ==================================================================================================================
document.addEventListener("DOMContentLoaded", () => {
  displayFeatures(features);
});
