# Kết quả kiểm tra

Ngày chạy: 28/10/2026. Môi trường: máy ảo Contacts_API35 với cấu hình Pixel 7, Android 15 / API 35, 1080 × 2400, RAM 1536 MB.

## Build và kiểm tra mã nguồn

Lệnh đã chạy thành công:

```bash
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest :app:testDebugUnitTest :app:lintDebug
```

- Build ứng dụng và APK kiểm thử thành công.
- `ContactRepositoryTest`: **3 tests, 0 failures, 0 errors**.
- Lint: **0 errors**, 9 cảnh báo gợi ý (tài nguyên/backup, số nhiều trong tiếng Việt, overdraw và phiên bản Gradle). Không tắt lint hoặc dùng baseline để bỏ qua lỗi.

## Kiểm thử giao diện

Đã cài hai APK lên máy ảo và chạy:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r com.example.myapplication.test/androidx.test.runner.AndroidJUnitRunner
```

Kết quả lượt chạy cuối: **OK (5 tests)** trong **83.759 giây**. [Log gốc của Espresso](ket_qua_espresso.txt).

| Luồng | Nội dung xác nhận |
| --- | --- |
| Đăng nhập | Chặn trống từng trường/cả hai, sai tên/sai mật khẩu; admin / 123456 mở danh sách 3 liên hệ, 1 yêu thích; Activity đăng nhập kết thúc. |
| Thêm liên hệ | Chặn tên trống, số ngắn/dài/đầu khác 0 và số trùng; mặc định Nam; chọn nhóm, yêu thích, ghi chú; thêm thành công thành 4 liên hệ/2 yêu thích; chi tiết giữ đủ thông tin. |
| Chi tiết | Bật yêu thích và quay lại cập nhật thống kê; mở ứng dụng quay số thực tế; xác nhận Intent ACTION_DIAL với tel:0901234567; Hủy giữ nguyên, Xóa còn 2 liên hệ/1 yêu thích. |
| Nhóm, giới tính và ghi chú tùy chọn | Đủ 4 nhóm; Nam mặc định, Nữ/Khác lưu đúng; ghi chú trống được chấp nhận; bỏ yêu thích cập nhật về 0 và ẩn sao. |
| Danh bạ rỗng | Xóa hết về 0 liên hệ/0 yêu thích, hiện hướng dẫn; thêm lại được với nhóm Gia đình, giới tính Nam và ghi chú trống. |

Đã lấy 13 ảnh chụp trực tiếp qua `UiAutomation.takeScreenshot()` trong [screenshots](screenshots/). Không có thông báo crash trong bộ lọc logcat `AndroidRuntime:E` sau lượt chạy cuối.

## Giới hạn đã biết

- Danh bạ chỉ ở trong RAM và trở về ba liên hệ mẫu khi tiến trình kết thúc, đúng phạm vi đề bài.
- Tài khoản admin / 123456 là tài khoản cố định cho bài thực hành.
- Đã chạy thực tế trên API 35; chưa kiểm thử trên mọi phiên bản/thiết bị thuộc phạm vi minSdk 24 trở lên.
- Nút Gọi điện mở trình quay số, không tự thực hiện cuộc gọi. Nếu thiết bị không có ứng dụng quay số, ứng dụng hiển thị thông báo.
