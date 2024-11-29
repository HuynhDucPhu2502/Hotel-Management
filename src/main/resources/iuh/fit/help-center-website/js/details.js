// ==================================================================================================================
// 1. Lấy dữ liệu câu hỏi
// ==================================================================================================================
// Lấy thông tin từ URL
const params = new URLSearchParams(window.location.search);
const featureName = params.get("name");

// Tìm dữ liệu câu hỏi và câu trả lời từ `questionsData`
const featureData = questionsData[featureName];

// ==================================================================================================================
// 2. Hiển thị giao diện các câu hỏi
// ==================================================================================================================
if (featureData) {
  // Hiển thị tiêu đề
  document.getElementById("feature-title").textContent = featureName;

  // Hiển thị câu hỏi và câu trả lời vào accordion
  const accordion = document.getElementById("accordionExample");
  featureData.forEach((item, index) => {
    const answerContent = item.answer
      .map((part) => {
        if (part.type === "text") {
          return `<p>${part.content}</p>`;
        } else if (part.type === "image") {
          return `<img src="${part.content}" alt="Mô tả ảnh" style="max-width: 100%; height: auto;" />`;
        }
        return "";
      })
      .join("");

    accordion.innerHTML += `
      <div class="accordion-item my-2">
        <h2 class="accordion-header" id="heading${index}">
          <button
            class="accordion-button collapsed" 
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#collapse${index}"
            aria-expanded="false"
            aria-controls="collapse${index}"
          >
            ${item.question}
          </button>
        </h2>
        <div
          id="collapse${index}"
          class="accordion-collapse collapse"
          aria-labelledby="heading${index}"
          data-bs-parent="#accordionExample"
        >
          <div class="accordion-body">
            ${answerContent}
          </div>
        </div>
      </div>
    `;
  });
} else {
  // Nếu không tìm thấy dữ liệu
  document.getElementById("feature-title").textContent =
    "Không tìm thấy nội dung!";
}
