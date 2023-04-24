# Tổng quan
Ứng dụng sử dụng giao thức UDP để truyền tin được viết bằng ngôn ngữ Java

# Các thành phần

## Giao diện
- Sử dụng java.awt để tạo các thành phần của giao diện: nút, hộp text...
- Sử dụng java.awt.event để xử lý tương tác của người dùng với các nút

## Client
- Client kết nối với giao diện cho phép người dùng nhập host, port và chọn file mà mình muốn gửi
- Client cho người dùng biết nếu có lỗi xảy ra và kích thước file, thời gian gửi file
- Client lấy metadata của file và gửi cho server, sau đó đọc, chia nhỏ rồi gửi dữ liệu file

## Server
- Cho phép người dùng chọn port để mở server
- Server nhận metadata từ client và xử lý nơi chứa file nhận
- Nhận file và viết vào luồng dữ liệu, sau đó viết vào file với metadata trước đó

# Ưu điểm

## Về ứng dụng
 - Mọi thứ gói gọn trong 1 app: Client, Server
 - Giao diện đơn giản, dễ hiểu

## Về file
Có thể gửi các loại file sau:
 - text + code (.txt, .java, .c, .cpp, .js, .html, .csv...)
 - video + âm thanh (mp3, mp4, wav...)
 - ảnh (png, jpg, gif...)
 - các file của microsoft office (.docx, .ppt, .xlsx...) + .pdf
 - exe
 - file nén (.zip, .rar, .jar...)
 - ...

## Về kích thước
 Có thể gửi các file có kích thước nhỏ (vài KB) đến các file có kich thước lớn (vài GB)

# Nhược điểm

## Về giao diện
 Nếu sử dụng Server và Client trên cùng 1 cửa sổ thì một số nhãn hiển thị (label) của Client và Server sẽ tranh chấp nhau

## Về file
 Một số loại file sau khi gửi thì không thể mở được vì ứng dụng vẫn "sử dụng" file (như file zip) và sẽ phải tắt ứng dụng mới có thể mở file

## Về tốc độ
 Tốc độ gửi trở nên chậm khi kích thước tăng cao (gửi file 1GB cần 40 phút)