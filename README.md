# SteelSafe
> 此插件为我的第一个minecraft插件,在10天时间内边摸索学习写出来的，存在很多问题，目前已经可以正常使用。

## 功能
  通过创建一个仅通过密码交互的保险箱，以完成各种交易、储存行为。目前的大部分箱子锁都需要其主主动给予权限，很是麻烦，而且不方便离线玩家的交易，所以有了这个插件。
  
  ----

## 功能/配置

~~~yml

# Configuration file
# 一个玩家可以拥有的最大保险箱数,默认是5，0表示不允许创建，必须是正整数！
MaxChest: 5
# op是否可以创建无数个箱子:true/false 默认true
OpChests: true
# 漏斗和漏斗矿车是否可以获取保险箱内的物品:true/false 默认false
Hopper: false
# 保险箱是否防爆：实体（tnt/creeper/...: true/false 默认false
EntityExplode: false
# 保险箱是否防爆：方块（bed/respawn_anchor/...）: true/false 默认false
BlockExplode: false
# 设置凋零能否破坏保险箱:true/false 默认false
WitherDestroy: false
# 设置末影龙能否破坏保险箱:true/false 默认false
EnderDragonDestroy: false
# 管理员是否可以打开任何保险箱（绕过密码验证）:true/false 默认false
OpOpen: true

~~~

## 命令

* /steelsafe[ss] help　　　　　　　　　　-列出所有命令
* /steelsafe[ss] create 密码 重复密码　　-以你面向的箱子创建一个密码保险箱。
* /steelsafekey[ssk] 密码　　　　　　　　-以密码尝试打开上锁的保险箱 
* /steelsafeshowkey[sssk]　　　　　　　　 -列出你的所有的保险箱
* /steelsafereload[ssr]　　　　　　　　　-重载插件配置[管理员]
* /steelsafereremove[ssre]　　　　　　　-强制删除面前的保险箱[管理员]

