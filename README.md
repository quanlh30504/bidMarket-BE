# bidMarket-BE
## Config environment

Để cấu hình môi trường cho ứng dụng, bạn cần tạo một file `.env` trong thư mục `resources` với nội dung như sau:

```plaintext
DB_URL='Đường dẫn đến DB'
DB_USERNAME= 'tên username của DB'
DB_PASSWORD= 'mật khẩu DB'
APP_JWT_SECRET=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q1r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2
FRONTEND_URL=http://localhost:3000
```

Thêm các thuộc tình trong .env cho cấu hình VNPay
```plaintext
PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
TMN_CODE=VEX87FEL
SECRET_KEY=16MWJWKRTFBYW7VSMDOUQ1K6C4WW2NSU
RETURN_URL=/vnpay-payment-return
VNP_API_URL=https://sandbox.vnpayment.vn/merchant_webapi/api/transaction
VNP_VERSION=2.1.0
VNP_COMMAND=pay
ORDER_TYPE=order-type
```
Config aws S3
```plaintext
AWS_S3_REGION=ap-southeast-1
AWS_S3_BUCKET=bidmarket-image-bucket
AWS_SECRET_ACCESS_KEY= 
AWS_ACCESS_KEY_ID=
DEFAULT_AVATAR_URL=
DEFAULT_PRODUCT_URL=
```

## Install and set up kafka in local computer
- Tải kafka và giải nén → đổi tên file giải nén thành kafka
- Khời chạy zookepper trên win

```plaintext
C:\kafka>bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

- Khời chạy kafka server trên win (chú ý mở cmd khác và để nguyên cmd đang chạy zookepper)

```plaintext
C:\kafka>bin\windows\kafka-server-start.bat config\server.properties
```
