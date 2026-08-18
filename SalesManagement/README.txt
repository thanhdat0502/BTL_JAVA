SALES MANAGEMENT SYSTEM - PHẦN MỀM QUẢN LÝ BÁN HÀNG
=====================================================

1. GIỚI THIỆU
Ứng dụng desktop quản lý bán hàng dùng cho bài tập lớn Java cơ bản. Chương trình
dùng Java Swing, lập trình hướng đối tượng và file TXT; hoàn toàn không dùng cơ sở
dữ liệu hay framework bên ngoài.

2. CHỨC NĂNG
- Đăng nhập, hiện/ẩn mật khẩu và đăng nhập bằng phím Enter.
- Dashboard: tổng sản phẩm, khách hàng, hóa đơn, doanh thu, hóa đơn gần nhất và
  biểu đồ doanh thu Java2D.
- CRUD sản phẩm, danh mục, khách hàng và nhân viên; tìm kiếm realtime.
- Cập nhật ảnh ngay trên từng dòng sản phẩm và hiển thị thumbnail trong bảng.
- Danh sách bán hàng dạng lưới 2 card mỗi hàng để hiển thị rõ ảnh, tên, mã,
  danh mục, giá, tồn kho, đã bán và nút thêm giỏ.
- Lọc sản phẩm theo danh mục, kiểm tra dữ liệu nhập và xác nhận trước khi xóa.
- Không cho xóa danh mục đang được sản phẩm sử dụng.
- Bán hàng: tìm/lọc sản phẩm, giỏ hàng, tăng/giảm/xóa số lượng, kiểm tra tồn kho,
  chọn khách hàng và nhân viên, thanh toán và xem ngay hóa đơn vừa tạo.
- Hóa đơn: tìm theo mã/khách hàng, lọc ngày theo yyyy-MM-dd, nhấp đúp để xem chi tiết.
- Thống kê doanh thu, số hóa đơn, số lượng bán, top 5 sản phẩm theo số lượng và
  top 5 khách hàng theo tổng giá trị các hóa đơn đã mua.

3. CÔNG NGHỆ
- Java 21 (mã nguồn chỉ dùng thư viện chuẩn).
- Java Swing, JTable, CardLayout, Java2D.
- OOP, ArrayList, File I/O, Exception Handling.
- Dữ liệu UTF-8, phân tách trường bằng ký tự |.

4. CẤU TRÚC
src/model/       Các lớp User, Product, Category, Customer, Employee, Invoice...
src/repository/  Đọc/ghi TXT và giao dịch lưu thanh toán.
src/service/     Validation và nghiệp vụ CRUD/thanh toán/thống kê.
src/view/        JFrame, JPanel và component Swing dùng lại.
src/utils/       Theme, định dạng tiền và validation.
data/            Toàn bộ dữ liệu của ứng dụng.
assets/icons/    Thư mục tài nguyên biểu tượng (có thể bổ sung).
tests/           Kiểm thử smoke test cho service và lưu file.

5. CÁCH CHẠY
Yêu cầu: Windows và Java 8 trở lên.
- Nhấp đúp run.bat; hoặc
- Mở terminal tại thư mục SalesManagement và chạy:
    java -jar SalesManagement_v1.6.jar

Chạy trong VS Code:
- Mở thư mục cha "BTL JAVA".
- Cài Extension Pack for Java nếu chưa có.
- Nhấn F5 và chọn "Run Sales Management".
- Nếu IDE vẫn giữ lỗi cũ: Ctrl+Shift+P > Java: Clean Java Language Server Workspace.

Chạy trong Eclipse:
- File > Import > Existing Projects into Workspace.
- Chọn thư mục SalesManagement, sau đó Run As > Java Application tại App.java.

Biên dịch lại từ mã nguồn (PowerShell):
    $files = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName
    javac -encoding UTF-8 -source 8 -target 8 -d build $files
    jar --create --file SalesManagementApp.jar --main-class App -C build .

6. TÀI KHOẢN MẶC ĐỊNH
Tên đăng nhập: admin
Mật khẩu:       123456
Vai trò:        ADMIN

7. LƯU DỮ LIỆU
Các file users.txt, products.txt, categories.txt, customers.txt, employees.txt,
invoices.txt và invoice_details.txt nằm trong data/. Khi CRUD, ArrayList được cập
nhật rồi ghi lại file UTF-8. Dòng dữ liệu hỏng được bỏ qua thay vì làm chương trình
crash. File/thư mục thiếu sẽ tự tạo. Thanh toán ghi các file liên quan theo nhóm và
khôi phục trạng thái bộ nhớ nếu ghi thất bại để tránh trừ tồn kho một phần.

8. GHI CHÚ TRÌNH BÀY
- MainFrame chỉ điều phối giao diện; nghiệp vụ nằm trong AppService.
- FileDataRepository chịu trách nhiệm duy nhất về TXT.
- CardLayout chuyển trang trong cùng một cửa sổ.
- DocumentListener giúp tìm kiếm cập nhật ngay khi gõ.
