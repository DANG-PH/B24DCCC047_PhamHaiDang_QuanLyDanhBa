# Đối chiếu bài làm với đề Quản lý danh bạ

Nguồn đối chiếu: `De_bai_Quan_ly_danh_ba.docx`, toàn bộ mục 1, 2.1–2.4 và 3.1–3.4. Đây là danh sách các yêu cầu của bài nộp và vị trí triển khai, không phải yêu cầu bổ sung cho ứng dụng.

## 1 Phạm vi

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Java và XML, đúng 4 màn hình | `LoginActivity`, `MainActivity`, `AddContactActivity`, `ContactDetailActivity` và 4 layout `activity_*.xml`; khai báo trong manifest. |
| Chuyển màn hình và truyền dữ liệu bằng Intent | `LoginActivity.login()`, `MainActivity.onCreate()`, `ContactDetailActivity.onCreate()`; Contact Serializable qua extra. |
| Lớp Contact có họ tên, số, nhóm, giới tính, yêu thích, ghi chú | `Contact.java`, đủ 6 trường nghiệp vụ; thêm ID để tìm đúng liên hệ khi thay đổi danh sách. |
| ArrayList trong bộ nhớ, không bắt buộc DB/API | `ContactRepository.contacts`; singleton dùng chung trong tiến trình; không có SQLite/Room/Firebase/API. |
| Có thể điều chỉnh màu/kiểu dáng nhưng giữ thành phần/chức năng | Giao diện xanh ngọc; không thay đổi bộ thành phần yêu cầu. |

## 2.1 Đăng nhập

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Màn hình đầu tiên khi mở app | Manifest chỉ đặt LAUNCHER cho LoginActivity. |
| EditText tên đăng nhập | `activity_login.xml`, `usernameInput`. |
| EditText mật khẩu ẩn | `passwordInput`, `inputType="textPassword"`. |
| Thông báo lỗi và nút Đăng nhập | `loginError` (ban đầu gone), `loginButton`. |
| Hard-code admin / 123456 | Hằng USERNAME và PASSWORD trong LoginActivity. |
| Không cho đăng nhập khi một trong hai trường trống | `login()`, kiểm tra `name.isEmpty() || pass.isEmpty()`; test từng trường và cả hai trống. |
| Sai thông tin báo đúng “Sai tên đăng nhập hoặc mật khẩu” và ở lại | `R.string.login_wrong`; kiểm thử sai tên và sai mật khẩu. |
| Đúng thì Intent sang MainActivity và finish | `login()`; test Activity đăng nhập ở trạng thái DESTROYED. |
| Ảnh và mô tả triển khai | 01, 02, 12 trong screenshots; mục đăng nhập, trang mã đăng nhập và hình kiểm tra dữ liệu trong báo cáo. |

## 2.2 Danh sách

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Thống kê tổng số và số yêu thích | `totalCount`, `favoriteCount`; MainActivity.onResume(), repository.getFavoriteCount(). |
| Cập nhật sau thêm, xóa, đổi yêu thích | onResume() nạp lại adapter và hai số thống kê; test thêm/xóa/bật/bỏ yêu thích. |
| ListView hoặc RecyclerView | `contactList` và `ContactAdapter extends BaseAdapter`. |
| Mỗi dòng đủ avatar chữ cái, tên, số, nhóm, dấu sao | `item_contact.xml`, ContactAdapter.getView(), Contact.getInitial(). |
| Chạm dòng mở chi tiết | OnItemClickListener tạo Intent và truyền đối tượng Contact. |
| Nút Thêm mở AddContactActivity | `addContactButton` và Intent tường minh. |
| Nguyễn Văn An, 0901234567, Bạn bè, không yêu thích | ContactRepository constructor; unit test và ảnh 03. |
| Trần Thị Bình, 0912345678, Gia đình, yêu thích | ContactRepository constructor; unit test và ảnh 03. |
| Lê Minh Châu, 0987654321, Công việc, không yêu thích | ContactRepository constructor; unit test và ảnh 03. |
| Ảnh/mô tả Adapter, thống kê, nút và sự kiện dòng | Ảnh 03/07; mục danh sách và trang mã MainActivity/ContactAdapter trong báo cáo. |

## 2.3 Thêm liên hệ

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Họ tên EditText, không để trống | `nameInput`, ContactValidator.isValidName(), setError và return. |
| Điện thoại EditText inputType phone, chỉ số | `phoneInput`, `inputType="phone"`, `digits="0123456789"`. |
| Đúng 10 chữ số và bắt đầu bằng 0 | isValidPhone() dùng `0[0-9]{9}`; kiểm tra số ngắn, dài, đầu khác 0, ký tự không phải số. |
| Spinner Gia đình, Bạn bè, Công việc, Khác | Mảng `contact_groups` trong strings.xml và ArrayAdapter. |
| RadioGroup Nam/Nữ/Khác, mặc định Nam | `genderInput`, `checkedButton="@id/genderMale"`; test Nam mặc định và hai lựa chọn còn lại. |
| CheckBox yêu thích | `favoriteInput`; trạng thái được đưa vào Contact. |
| Ghi chú EditText nhiều dòng, không bắt buộc | `notesInput`, inputType chứa textMultiLine; test lưu với ghi chú trống. |
| Nút Lưu liên hệ | `saveContactButton` gọi save(). |
| Chặn tên trống/số sai, báo setError hoặc Toast | Các nhánh kiểm tra trong save(), không thêm phần tử khi sai. |
| Không lưu số đã có | repository.containsPhone() trong save() và kiểm tra lại trong add(). |
| Tạo Contact đầy đủ trường, về danh sách, thấy liên hệ và thống kê mới | save() tạo Contact, repository.add(), finish(); test 4 liên hệ / 2 yêu thích và mở lại chi tiết. |
| Ảnh/mô tả thành phần, validation, tạo đối tượng và code | Ảnh 04/05/06/07; mục thêm, trang mã AddContactActivity/ContactValidator và trích XML trong báo cáo. |

## 2.4 Chi tiết

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Họ tên và avatar chữ cái đầu | `detailName`, `detailAvatar`. |
| Số điện thoại, nhóm, giới tính, ghi chú | `detailPhone`, `detailGroup`, `detailGender`, `detailNotes`. |
| Nhận dữ liệu qua Intent | IntentCompat.getSerializableExtra(), sau đó tìm Contact gốc bằng ID. |
| CheckBox bật và bỏ yêu thích | `detailFavorite`, OnCheckedChangeListener; test cả hai chiều, đếm lại và ẩn sao khi bỏ. |
| Nút Gọi điện dùng Intent ngầm định ACTION_DIAL | `callButton`, Uri.fromParts("tel", phone, null); test ACTION_DIAL và số đích; mở trình quay số thực tế. |
| Nút Quay lại đóng màn hình về danh sách | `backButton` được BaseActivity gắn finish(); kiểm thử sau xem/thay đổi liên hệ. |
| Nút Xóa loại khỏi ArrayList, về danh sách, cập nhật thống kê | `deleteButton`, AlertDialog xác nhận, repository.delete(id), finish(). Hủy không xóa. |
| Ảnh/mô tả thông tin, Intent, quay lại, xóa và cập nhật | Ảnh 08/09/10/11; mục chi tiết, trang mã, mục xóa của báo cáo. |

## 3.1–3.4 Sản phẩm và báo cáo

| Yêu cầu | Vị trí thực hiện / minh chứng |
| --- | --- |
| Toàn bộ dự án Android Studio trên Git, không ZIP | Repository có app/, Gradle wrapper, cấu hình, Java/XML; không có ZIP bài nộp. |
| .gitignore loại build, .gradle, .idea, local.properties | .gitignore gốc; kiểm tra git ls-files không chứa các mục này. |
| Public hoặc cấp quyền cho giảng viên | Repository public, xác nhận từ GitHub API ngày 28/10/2026. |
| Báo cáo Word hoặc PDF | docs/B24DCCC047_PhamHaiDang_BaoCao.docx. |
| Mỗi màn hình có ảnh chụp đang chạy VÀ mô tả | Đủ 4 mục màn hình, ảnh gốc trong screenshots và chú thích dưới ảnh. |
| Mô tả ngắn, rõ component/lớp/hàm/sự kiện | Các mục màn hình và trang mã tương ứng. |
| Có thể kèm code tiêu biểu; người dùng yêu cầu bổ sung | Báo cáo có trích Java từ nguồn thật, tên file/số dòng; các thuộc tính XML được trích và chú thích phần lược bỏ. |
| Ảnh rõ, không cắt khung màn hình, có chú thích | Ảnh PNG 1080 × 2400, giữ tỷ lệ toàn màn hình trong Word; kiểm tra bản render từng trang. |
| Tên báo cáo đúng mẫu | B24DCCC047_PhamHaiDang_BaoCao.docx. |
| Đầu báo cáo có họ tên, MSSV, lớp và link Git | Phạm Hải Đăng, B24DCCC047, D24CQPM03, link repository ở trang đầu. |
| Tên repository đúng mẫu | B24DCCC047_PhamHaiDang_QuanLyDanhBa. |
| Mô tả khớp ảnh và mã nguồn | Ảnh chụp khi chạy Espresso; snippet Java do tools/generate_report.py đọc trực tiếp từ mã nguồn. |
| Ứng dụng chạy và không crash trong các thao tác đã kiểm tra | Build, lint, unit test, Espresso trên API 35; kết quả trong KIEM_THU.md và log gốc. |

Các lựa chọn “nên chụp” và “có thể kèm” trong đề là gợi ý; bản cập nhật vẫn bổ sung ảnh lỗi, quay số và mã tiêu biểu để minh chứng đầy đủ hơn. Chưa kiểm thử trên mọi thiết bị Android; dữ liệu chỉ tồn tại trong vòng đời tiến trình theo phạm vi đề.
