# Quản lý danh bạ liên hệ

Bài thực hành Android Java/XML của **Phạm Hải Đăng — B24DCCC047 — D24CQPM03**.

Repository: https://github.com/DANG-PH/B24DCCC047_PhamHaiDang_QuanLyDanhBa

## Chạy ứng dụng

1. Mở thư mục dự án bằng Android Studio. Cài Android SDK 37 và JDK 25 (Gradle daemon có cấu hình tự tải JDK).
2. Đợi Gradle Sync, chọn thiết bị Android 7.0 / API 24 trở lên rồi bấm Run.
3. Đăng nhập bằng tài khoản cố định **admin / 123456**.

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest :app:lintDebug
# Cần máy ảo hoặc điện thoại đã kết nối adb:
./gradlew :app:connectedDebugAndroidTest
```

APK debug sau khi build: `app/build/outputs/apk/debug/app-debug.apk`.

## Chức năng theo đề

- `LoginActivity`: kiểm tra bỏ trống, sai tài khoản, ẩn mật khẩu; mở danh sách bằng Intent và `finish()` khi đăng nhập thành công.
- `MainActivity`: ListView với adapter hiển thị avatar chữ cái đầu của tên, họ tên, số điện thoại, nhóm, dấu sao; thống kê tổng liên hệ/yêu thích trong `onResume()`.
- `AddContactActivity`: tên, điện thoại, Spinner 4 nhóm, RadioGroup 3 giới tính (mặc định Nam), CheckBox yêu thích, ghi chú nhiều dòng. Chặn tên trống, số không khớp `0[0-9]{9}` và số trùng.
- `ContactDetailActivity`: nhận đối tượng `Contact` qua Intent, hiển thị đầy đủ thông tin, thay đổi yêu thích, quay lại, xác nhận xóa và mở trình quay số với `ACTION_DIAL` (không tự thực hiện cuộc gọi).
- `ContactRepository`: `ArrayList<Contact>` dùng chung trong bộ nhớ, khởi tạo đúng 3 liên hệ mẫu của đề. Không dùng cơ sở dữ liệu hoặc API. Dữ liệu trở về mẫu khi tiến trình ứng dụng bị kết thúc.
- `BaseActivity`: xử lý khoảng trống thanh hệ thống và bàn phím để nội dung không bị che trên Android mới. Các biểu mẫu có thể cuộn trên màn hình nhỏ.

## Cấu trúc

```text
app/src/main/java/com/example/myapplication/
  Contact.java                # Mô hình Serializable, ID ổn định
  ContactRepository.java      # Kho ArrayList, thêm/xóa/đếm yêu thích
  ContactValidator.java       # Kiểm tra tên và số điện thoại
  ContactAdapter.java         # Dòng liên hệ của ListView
  BaseActivity.java           # Insets và nút quay lại
  LoginActivity.java
  MainActivity.java
  AddContactActivity.java
  ContactDetailActivity.java
app/src/main/res/layout/       # 4 màn hình và item_contact.xml
app/src/test/                 # Kiểm tra validator và kho danh bạ
app/src/androidTest/          # Kiểm thử luồng UI bằng Espresso
```

## Báo cáo và minh chứng

- [Báo cáo Word](docs/B24DCCC047_PhamHaiDang_BaoCao.docx)
- [Ảnh chụp ứng dụng đang chạy](docs/screenshots/)
- [Kết quả kiểm tra](docs/KIEM_THU.md)

Báo cáo có thông tin sinh viên, hình ảnh và mô tả triển khai của cả 4 Activity. Ảnh được chụp từ máy ảo Pixel 7 bằng UI Automation trong các kịch bản Espresso, không phải ảnh giao diện dựng lại.

Dự án giữ cấu hình Gradle/AGP/SDK của Android Studio ban đầu: Gradle 9.6.0, AGP 9.4.1, compile/target SDK 37, mã nguồn Java 11, Gradle daemon JDK 25. Không commit `.idea`, `.gradle`, `build`, `local.properties` hoặc APK.
