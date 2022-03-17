# Story-1

```
As a 用户
I want to 在自助存取包服务终端查看到储物柜(locker)是否可用
So that 我可以存包
```

**AC1：**

**Given** locker有可用箱位(slot)

**When** 我已经进入到存取包服务终端

**Then** 我能在页面查看到locker状态为”储物柜有可用空间，请存包“

**AC2：**

**Given** locker没有有可用箱位(slot)

**When** 我已经进入到存取包服务终端

**Then** 我在页面查看到locker状态为”储物柜已满“

# Tasking
**假设**：故事卡是全栈不分前后端的。但团队中会有多对pair分别focus在前端和后端上，他们会共同实现一张故事卡。

## 面向业务Tasking（BOT）: 
- **识别业务需求**：前端实时刷新状态 or 手动刷新状态？
  - 业务的回答：前端定时刷新
- **识别业务需求**：查询是否有延迟？是否需要展示Loading？
  - 业务的回答：查询延迟极低，不需要Loading
- **识别业务需求**：是否有访问安全要求？
  - 业务的回答：设备前后端都处于内网，不需要额外的安全保护
- **识别业务需求**：通用错误如何处理(系统500错误，超时等)？
  - 业务的回答：展示系统不可用界面
  - 新增 **AC3** (Sad path)
    - **Given** locker服务返回500错误/无法连接/响应超时

      **When** 我已经进入到存取包服务终端

      **Then** 展示“暂停服务“页面
      
- **识别上线部署要求**：
  - TL的回答：设备的软件更新由OTA功能管理，支持远程推送升级，不需要手动操作。
  - TL的回答：设备的配置（如slot数量）会在软件启动时自动通过环境变量写入数据库。LockerService只依赖数据库中的数据。

## 面向解决方案Tasking（SOT）:

### Tasking for AC1

### 基于进程间架构
- **识别**：进程间架构是什么样的？
  - 涉及两个进程间组件：LockerWeb(静态web) + LockerService（spring）
  - 假设：我们这对Pair focus在后端服务
- **LockerService API设计**
  - Request: `GET /locker`
  - Response: 
    - Code`200 ok`
      - Body: `{ "hasAvailableSlot": boolean }`
      - Description: 
        - `true`: 有可用空位；
        - `false`：无可用空位
- **LockerService Tasking 描述**

  **Given** locker有可用箱位(slot)

  **When** 调用API`GET /locker`

  **Then** API返回`200 ok；{ "hasAvailableSlot": true }`

### 基于进程内架构 (LockerService)

#### 1. controller

```text
设计：新方法 lockerService.getLockerStatus(): LockerStatus {hasAvailableSlot: boolean}

Given：lockerController收到请求，并通过lockerService获取locker的status

When：向 /locker发送get请求

Then get /locker response返回Object LockerStatus {hasAvailableSlot: true}
```

#### 2. service

```text
设计：新方法 lockerRepository.getLockerStatus(): LockerStatus {hasAvailableSlot: boolean}

Given：lockerService.getLockerStatus，通过lockerId，向lockerRepository获取locker status

When：lockerController 通过lockerService.getLockerStatus 获取locker状态

Then： lockerService.getLockerStatus应该返回Object LockerStatus {hasAvailableSlot: true}
```

#### 3. data source

```text
设计：新方法 lockerRepository.countByHasBagFalseAndLockerId(): long

Given：数据库中对应的locker有empty slot

When：lockerService 通过lockerRepository.countByHasBagFalseAndLockerId 获取对应locker的empty slot数量

Then： lockerRepository.countByHasBagFalseAndLockerId应该返回empty slot的数量
```

### LockerService Tasking 2

**AC2:**

**Given** locker没有可用箱位(slot)

**When** 存取包服务终端向服务发送get /locker status的请求

**Then** /locker返回的status是：没有可用的slot

#### tech tasking
自己尝试一下吧 ：P


### LockerService Tasking 3

**AC3:**

**Given** locker服务返回500错误/无法连接/响应超时

**When** 我已经进入到存取包服务终端

**Then** 展示“暂停服务“页面

#### tech tasking

- API设计
  - Request: `GET /locker`
  - Response: 
    - Code`200 ok`: 略
    - Code `500 internal server error`
      - Description: 服务不可用

自己尝试一下吧 ：P

**思考题**: 
1. tech tasking应该在开始开发前完成还是开发中逐步实现？
2. 使用Tasking + TDD时，应该在何时进行设计（架构、类、方法、设计模式、数据结构、算法）？
