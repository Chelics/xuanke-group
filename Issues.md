# 数据库密码字段加长
student表, teacher表, staff表的password字段长度改为255, 因为加密后的密码会很长

# Interceptor拦截
目前除了"/login"不会被拦截, 所有页面均会被拦截, 拦截后会在前端的msg中显示"Not log in"
解决方法: 
1. 在数据库中添加账户信息: 其它字段照常添加, password字段需要添加加密后的密码.
    - 如何生成加密后的密码? 可在service下的UnitTest类中执行testEncodePassword方法生成"123456"加密后的密码, 复制到数据库表中(也可以将123456替换为其它密码)
2. 获取JWT令牌: 在Apifox中发送登录请求, 注意参数在body里面, 不在param, 填写数据库中匹配的username和明文密码, 返回sucess后复制data中的JWT令牌
3. 添加JWT令牌到请求中: 在要发送的请求中, 添加Header项: 参数名为token, 参数值为刚刚复制的JWT令牌, 此时发送请求将会被Interceptor放行

==粗暴的解决方法: 在config包下的WebConfig类中, 将`registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/\**").excludePathPatterns("/login");
中的/login改为要发送的请求路径(包含所有子路径加/**), 即可使该请求不被Interceptor拦截==

# 问题: username命名规则
如何定义username命名规则以区分教师, 学生和教务: 
com/seu/service/impl/LoginServiceImpl.java:22

当前设定: username第一位: 0表示学生, 1表示老师, 2表示教务, 如果是其他数字直接返回用户名或密码错误~

# 问题: 改用 HTTPS传输协议
用户登录时, 密码以明文形式从客户端传到服务端, 存在风险
方案一: 在前端用哈希, 盐和公钥等对密码进行加密, 传输密文
方案二: 改用HTTPS传输协议, 除了密码之外其他传输的信息也可以被加密, 大大降低了传输过程中泄露的风险, 但是需要SSL证书
- 自签名证书可以满足使用需要, 但是用户会收到不安全的提醒, 降低用户信任度
- 由受信任的证书颁发机构颁发的证书是最佳选项, 需要一点点钱:
  - 域名的价格是几十元
  - SSL证书的价格几十到几百不等, Sectigo上DV级别的单域名证书价格在99美元/年, 然而似乎也有一些方法可以免费获取一些受信任的SSL证书

# 问题: 教学楼的表示方式
"J1"/"教1"/"教学楼1"?, 目前采用"教1"表示

# 问题: 学生的全局搜索应该能搜索哪些字段
当前能搜索到的字段: 课程名, 课程编号, 开设学院, 教师姓名, 教室

# 问题: 各功能下返回的课程列表, 其中的课程应该按什么排序
当前排序规则: 未排序

# 问题: 如何判断当前所属的选课阶段可以选课
当前判断依据: 根据阶段名称是否含有subString("-开放选课")或subString("退改补")判断
