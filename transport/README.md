# Git flow

### Giả định

* Đã tạo Central Repository trên Gitlab（hoặc Github/Bitbucket）.
* Branch mặc định của Central Repository là `develop`.
* Lập trình viên có thể  fork hoặc tạo nhánh đối với Central Repository.
* Đã quyết định người review và người có quyền merge.

### Nguyên tắc

* Khi làm task phải tạo ra nhánh dựa trên nhánh `develop` để thực hiện.
* Nếu feature lớn cần nhiều dev làm chung thì sẽ tạo ra base-branch chung dựa trên nhánh `develop`, sau đó các dev cùng phát triển (tách nhánh) trên base branch mới này.
* Khi phát triển trên máy dev hoàn tất (đã test trên local) thì có thể tạo merge-request vào base-branch trên Gitlab.
* Mỗi merge-request tương ứng với một issue.
* Mỗi một merge-request không hạn chế số lượng commit
* Nội dung commit nên rõ nghĩa và tuân theo convention (tham khảo [Git Convention](https://www.conventionalcommits.org/en/v1.0.0-beta.4/))
* Lưu ý, do khi sử dụng `force-push` sẽ xoá hết lịch sử thay đổi do vậy không khuyến khích sử dụng `force-push`. Trong trường hợp cần sử dụng force push thì cần có sự đồng thuận từ team.
* Tại môi trường local (trên máy dev), tuyệt đối không được thay đổi code khi ở base branch `master` và `develop`. Nhất định phải thao tác trên nhánh khởi tạo để làm task.
* Tất cả các lệnh merge ở dưới local cần sử dụng option `--no-ff`, (ví dụ merge nhánh `release` vào nhánh `master`: `git merge release-1.1.0 --no-ff`)

### Chuẩn bị

1. Trên Gitlab (Github/Bitbucket), fork Central Repository về tài khoản của mình（repository ở tài khoản của mình sẽ được gọi là Forked Repository）.

2. Clone (tạo bản sao) Forked Repository ở môi trường local.Tại thời điểm này, Forked Repository sẽ được tự động đăng ký dưới tên là `origin`.
    ```sh
    $ git clone [URL của Forked Repository]
    ```

3. Truy cập vào thư mục đã được tạo ra sau khi clone, đăng ký Central Repository dưới tên `upstream`.
    ```sh
    $ cd [thư mục được tạo ra]
    $ git remote add upstream [URL của Central Repository]
    ```

### Quy trình

Từ đây, Central Repository và Forked Repository sẽ được gọi lần lượt là `upstream` và `origin`.

1. Đồng bộ hóa branch develop tại local với upstream.
    ```sh
    $ git checkout develop
    $ git pull upstream develop
    ```

2. Tạo branch để làm task từ branch develop ở local. Tên branch là số issue của task（Ví dụ: `feat/${username}_1234`）.
    ```sh
    $ git checkout develop # <--- Không cần thiết nếu đang ở trên branch develop
    $ git checkout -b feat/${username}_1234
    ```

3. Tiến hành làm task（Có thể commit bao nhiêu tùy ý）.

4. Push code lên origin.

    ```sh
    $ git push origin feat/${username}_1234
    ```

5. Tại origin trên Gitlab（Github/Bitbucket）、từ branch `feat/${username}_1234` đã được push lên hãy gửi merge-request đối với branch develop của upstream.

6. Hãy gửi link URL của trang merge-request cho reviewer trên Skype để tiến hành review code.

    6.1. Trong trường hợp reviewer có yêu cầu sửa chữa, hãy thực hiện các bước 3. 〜 5..
    6.2. Tiếp tục gửi lại URL cho reviewer trên Skype để tiến hành việc review code.

7. Nếu trên 2 người reviewer đồng ý với merge-request, merge-request có thể được merge.
   Reviewer xác nhận sự đồng ý bằng comment "LGTM".

8. Quay trở lại 1.

#### Khi xảy ra conflict trong quá trình rebase

Khi xảy ra conflict trong quá trình rebase, sẽ có hiển thị như dưới đây (tại thời điểm này sẽ bị tự động chuyển về một branch vô danh)
```sh
$ git rebase develop
First, rewinding head to replay your work on top of it...
Applying: refs #1234 Sửa lỗi cache
Using index info to reconstruct a base tree...
Falling back to patching base and 3-way merge...
Auto-merging path/to/conflicting/file
CONFLICT (add/add): Merge conflict in path/to/conflicting/file
Failed to merge in the changes.
Patch failed at 0001 refs #1234 Sửa lỗi cache
The copy of the patch that failed is found in:
    /path/to/working/dir/.git/rebase-apply/patch

When you have resolved this problem, run "git rebase --continue".
If you prefer to skip this patch, run "git rebase --skip" instead.
To check out the original branch and stop rebasing, run "git rebase --abort".
```

1. Hãy thực hiện fix lỗi conflict thủ công（những phần được bao bởi <<< và >>> ）.
Trong trường hợp muốn dừng việc rebase lại, hãy dùng lệnh `git rebase --abort`.

2. Khi đã giải quyết được tất cả các conflict, tiếp tục thực hiện việc `rebase` bằng:

    ```sh
    $ git add .
    $ git rebase --continue
    ```

## Quy trình Release

* Nhánh `develop` sẽ được deploy lên môi trường test
* Nhánh `master` sẽ được deploy lên môi trường production
* Khi code trên nhánh `develop` đã ổn định, cần release version mới thì sẽ tạo nhánh `release-x.y.z`
 (ví dụ, `release-1.1.0`) dựa trên nhánh develop.
* Nâng version trong source code tại nhánh `release-x.y.z`, commit thay đổi và merge vào nhánh 
`master`, đánh git-tag theo version phát hành
* Khi có lỗi trên môi trường test, tạo nhánh fix bug `bug/${username}_xxx` dựa trên `develop`
* Khi có lỗi trên production, tạo nhánh `hotfix/${username}_xxx` dựa trên `master`, nhánh hotfix sẽ 
được merge vào cả `master` và `develop`, release version fixbug.
* Số version cần tuân theo Semver convention (https://semver.org/)

# Deployment

- Hiện tại, project sử dụng file `jar` đã được nhúng thư viện tomcat bên trong khi deploy, không sử 
dụng file `war` và server tomcat.

### Build source code

- Project đang sử dụng build tool là **gradle** và có sử dụng gradle wrapper nên chỉ cần pull 
source code về là có thể build source code, không cần cài đặt thêm gradle trên máy. Có 2 cách để 
thực hiện build:

- Build toàn bộ project, ví dụ:

```bash
# Build toàn bộ project
./gradlew clean build
```

- Build chỉ định module, ví dụ:

```bash
# Build chỉ định module transport-api
./gradlew clean build -p transport-api
```

- Sau khi build xong, file `jar` được đặt trong thư mục build của từng module, ví dụ 
`transport/transport-api/build/libs/transport-api.jar`

### Deploy

- Sử dụng lệnh `java -jar` để deploy bằng terminal:

Ví dụ:
```bash
# Sử dụng spring-profile mặc định
java -jar /path/to/transport-api.jar
```

- spring-profile mặc định được chỉ định trong file *application.properties* hoặc *application.yml* 
tương ứng với từng module với property key là `spring.profiles.active`.

- Chỉ định spring-profile khi deploy, spring-profile là phần suffix của file 
*application-xyz.properties* hoặc *application-xyz.yml*:

```bash
# Chỉ định spring-profile=hason_haivan-test khi deploy server test
java -Dspring.profiles.active=vungtau-test -jar /path/to/transport-api.jar
```

- Vì khi sử dụng lệnh `java -jar` sẽ cần phải duy trì session terminal, vậy nên khi deploy code 
trên server ta sẽ cần wrap command này bằng systemd service.

- Trên server tạo một file systemd service tại thư mục `/etc/systemd/system/`, ví dụ:

```config
[Unit]
Description=transport-api-vungtau
After=syslog.target

[Service]
EnvironmentFile=/path/to/transport-api.cfg
User=app
ExecStart=/usr/bin/java -Dspring.profiles.active=vungtau-test -jar /path/to/transport-api.jar
SyslogIdentifier=transport-api-vungtau
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
```

```bash
# transport-api.cfg
# vungtau-test

# Vung Tau
SERVER_PORT=8081
SERVER_SERVLET_CONTEXT_PATH="/transport"

# Logging
LOGGING_FILE_PATH="/root/vungtau/deploy/log"

# Datasource
SPRING_DATASOURCE_URL="jdbc:mysql://103.141.140.12:3306/db_phatlocan?useLegacyDatetimeCode=false&serverTimezone=GMT%2B7&useUnicode=true&characterEncoding=utf8"
SPRING_DATASOURCE_USERNAME="sql_trung_chuyen_phatlocan"
SPRING_DATASOURCE_PASSWORD="Sqltrungchuyen@123"
# 30 seconds
SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=30000
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=50
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5
# 2 minutes
SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT=120000
# 30 minutes
SPRING_DATASOURCE_HIKARI_MAX_LIFETIME=1800000
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

# Spring redis datasource
SPRING_REDIS_HOST="3.1.31.233"
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD="HavzReeedisjYR8r25tyZ2011"

# Havaz
# JWT Secret Key
HAVAZ_SECURITY_JWT_SECRET_KEY="d2c1ac4bdb07052d27fa6ec40bb7da11c19adc1e77425e9e9f9036cda001d38a66ec0ca6bcf2193ca515155075fa6bc8c21ef30d77b0c4e2a9f1e1cc7feb502c"

# MQTT
HAVAZ_MQTT_URL="wss://emqx.havazdev.net:8087"
HAVAZ_MQTT_USERNAME="mqtt"
HAVAZ_MQTT_PASSWORD="HazClients@123"
HAVAZ_MQTT_TRACKING_DRIVER="VungTau_queue_driver"
HAVAZ_MQTT_QUEUE_TRACKING_DRIVER="VungTau_queue_driver"

# Havaz redis key
HAVAZ_REDIS_KEY="PHATLOCAN_KEY"
HAVAZ_REDIS_PUBSUB_CHANNEL="notify_to_manager_vungtau_dev"

# RabbitMQ Sàn
HAVAZ_RABBITMQ_SAN_HOST="202.134.19.11"
HAVAZ_RABBITMQ_SAN_PORT=5672
HAVAZ_RABBITMQ_SAN_USERNAME="shavaz"
HAVAZ_RABBITMQ_SAN_PASSWORD="HazavSA12B4nvE@2020"
HAVAZ_RABBITMQ_SAN_VIRTUAL_HOST="/"
HAVAZ_RABBITMQ_SAN_ON_SEND_MESSAGE=true
HAVAZ_RABBITMQ_SAN_EXCHANGE="transport.tracking"
HAVAZ_RABBITMQ_SAN_ROUTING_KEY="transport.tracking.updatepickuptime"
HAVAZ_RABBITMQ_SAN_DRIVER_ARRIVED_QUEUE="transport.tracking.driver"
HAVAZ_RABBITMQ_SAN_DRIVER_ARRIVED_ROUTING_KEY="transport.tracking.driver.has.arrived"
HAVAZ_RABBITMQ_SAN_DRIVER_CONFIRMED_QUEUE="transport.rabbitmq.driver.confirmed"
HAVAZ_RABBITMQ_SAN_DRIVER_CONFIRMED_ROUTING_KEY="transport.rabbitmq.driver.confirmed.routing"

# RabbitMQ ERP
HAVAZ_RABBITMQ_ERP_HOST="103.141.140.12"
HAVAZ_RABBITMQ_ERP_PORT=5672
HAVAZ_RABBITMQ_ERP_USERNAME="erp"
HAVAZ_RABBITMQ_ERP_PASSWORD="havaz@erp"
HAVAZ_RABBITMQ_ERP_VIRTUAL_HOST="vungtau"
HAVAZ_RABBITMQ_ERP_QUEUE_TRANSFER_TICKET="3-queue-transshipments-ticket"
HAVAZ_RABBITMQ_ERP_QUEUE_TRANSFER_TICKET_SYNC="3-sync-queue-transshipments-ticket"

HAVAZ_RABBITMQ_TRANSPORT_TRANSFERTICKET_EXCHANGE="3-transshipments-ticket"
HAVAZ_RABBITMQ_TRANSPORT_TRANSFERTICKET_ROUTING_KEY="transport.transferticket.update"
HAVAZ_RABBITMQ_TRANSPORT_TRANSFERTICKET_QUEUE="3-queue-update-ticket"

HAVAZ_STOMP_TOPIC="/topic/noti"
HAVAZ_FIREBASE_CMD_HAVAZNOW_TOPIC="/topics/cmdhavaz-now-"
HAVAZ_SMS_TOKEN="664a5c3dddff20de79e38111025c01f3"
HAVAZ_SMS_HOTLINES=""
HAVAZ_TIME_DELAY=5
HAVAZ_NUMBER_DRIVER_LIMIT=2
HAVAZ_NUMBER_MINUTE_LIMIT=60000
HAVAZ_URL_ERP="https://dev.vungtau.havaz.vn/api/transshipment/"
HAVAZ_URL_MONITOR_TRANSFER_TICKET="https://dev.admin.vungtau.havaz.vn/cron/"
HAVAZ_URL_ERP_TOKEN="664a5c3dddff20de79e38111025c01f3"

# Quartz
SPRING_QUARTZ_PROPERTIES_THREADPOOL_THREADCOUNT=4
HAVAZ_QUARTZ_CRON_EXPRESSION_MONITOR_TRANSFER_TICKET="0 */5 * ? * * *"
```

- Sau khi tạo mới service hoặc cập nhật, ta cần chỉ định cho server reload service:
```bash
systemctl daemon-reload
```

- Sau khi đã có service, ta cần đường dẫn file đúng như đã khai báo với systemd service và sử 
dụng các lệnh sau để deploy hoặc kiểm tra trạng thái deploy:

**API**
```bash
$ systemctl status vungtau_api.service
$ systemctl start vungtau_api.service
$ systemctl stop vungtau_api.service
$ systemctl restart vungtau_api.service
```

**BATCH**
```bash
$ systemctl status vungtau_batch.service
$ systemctl start vungtau_batch.service
$ systemctl stop vungtau_batch.service
$ systemctl restart vungtau_batch.service
```

### Logging

- File log của ứng dụng được đặt tại đường dẫn tương ứng với giá trị của property key `logging.path`
 trong file *application-xyz.properties* của từng module.