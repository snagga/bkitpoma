<font size='5'>HƯỚNG DẪN SỬ DỤNG PHẦN MỀM POMA TEST</font>

---


<br />
<font size='3'> Mục lục </font>


# Mục đích #
-  Dùng để giả lập quá trình gửi tín hiệu từ thiết bị lên server. Người dùng sử dụng dữ liệu mẫu có sẵn để kiểm tra hoạt động của thiết bị trên bản đồ. <font color='#0070C0'>(Xem thêm phần hiển thị, theo dõi thiết bị trong bản đồ ở mục Hướng dẫn sử dụng).<br>
</font>
<br />

# Cách sử dụng #

-  Cần file POMATest.exe và POMAMobileApi.dll.
-  Giao diện của file POMATest.exe

![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image002.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image002.jpg)

<font color='#4F81BD'>Hình 1: Giao diện chính của chương trình Test với các thành phần được chú thích bên cạnh</font>

-  Người dùng nhập vào URL của service (ở localhost thì để là [http://localhost:8080/api/mobile](http://localhost:8080/api/mobile); ở trên web thì thay thế localhost&nbsp;:8080 bằng địa chỉ tương ứng của trang web). Sau đó nhập vào ID của thiết bị ở khung Username và nhập vào mật khẩu. Ấn nút Login, khung chứa thông báo của chương trình sẽ xuất ra đăng nhập thành công (nếu localhost đang chạy trên máy mình hoặc đang test trực tiếp trên trang web).
-  Dữ liệu test được nhập vào Khung nhập dữ liệu về đường đi của thiết bị, theo định dạng mỗi dòng gồm 2 số thập phân cách nhau bằng 1 dấu phẩy, số thứ nhất là vĩ độ, số thứ 2 là kinh độ của điểm mà thiết bị đi qua. (Có 1 số file dữ liệu mẫu cho người dùng test Track2 _HVBC.txt, Track4_ RMIT\_CONIC.txt, Track1.txt).
-  Nút Clear dùng để xóa dữ liệu trong khung chứa thông báo của chương trình.
-  Nút Pause/Start dùng để tạm ngưng / tiếp tục quá trình truyền dữ liệu lên server.
-  Người dùng có thể chọn khoảng thời gian muốn gửi tín hiệu lên server (mặc định là bằng thời gian gửi dữ liệu của chính thiết bị đó).
-  Nút new track dùng để tạo ra ID mới cho đường đi của thiết bị.

# Hình ảnh minh họa #

![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image004.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image004.jpg)

<font color='#4F81BD'>Hình 2: Dữ liệu lần lượt được thêm vào và cập nhật tương ứng trên bản đồ</font>

![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image006.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image006.jpg)

<font color='#4F81BD'>Hình 3: Dữ liệu mẫu, copy vào clipboard</font>

![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image008.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Poma%20Test%20Guide/image008.jpg)

<font color='#4F81BD'>Hình 4: Thêm dữ liệu vào chương trình Poma Test, bấm ’Insert’ để tiến hành test</font>