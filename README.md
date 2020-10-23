# SteelSafe
> 此插件为我的第一个minecraft插件,在10天时间内边摸索学习写出来的，存在很多问题，目前已经可以正常使用。

## 功能
  通过创建一个仅通过密码交互的保险箱，以完成各种交易、储存行为。目前的大部分箱子锁都需要其主主动给予权限，很是麻烦，而且不方便离线玩家的交易，所以有了这个插件。
  
## 支持
**插件支持1.13.x-1.16.x**
兼容：**PlotSquared、Residence、worldguard**
  
  ----

## 功能/配置

~~~yml

# SteelSafe(密匙保险箱) Configuration
# 作者：Iseason
# 个人博客：https://www.iseason.top
# 插件版本:beta_1.0

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
# 提示信息：
# 创建箱子时，箱子已上锁的提示 "%chest%"表示箱子的坐标,"%player%"表示箱子的创建者。
CreateFailure: "&e这个箱子(&d%chest%&e)已经上锁了！所有者:&a%player%)"
# 保险箱创建成功的提示语 "%chest%"表示箱子的坐标,%key%表示保险箱密码。
CreateSuccess: "&a箱子&6%chest%&a已上锁，密码：&e%key%"
# 没有一些地方的权限时的提示语
HaveNoPermission: "&c你没有此地的&4箱子&c权限，无法使用此命令！"
# 尝试打开一个上锁的保险箱时 %player%"表示箱子的创建者。
TryToOpen: "&e这个箱子已经上锁了，所有者：&6%player%,&e请使用&l /ssk 密码 &r&e解锁此箱子。"
# 当箱子没有上锁，但是使用了ssk命令时的提示语。
UnLocked: "&e这个箱子没有上锁！"
# 当只用命令（创建/打开）后面向的方块不是箱子时的提示语。
NotChest: "&e请对着&l箱子/陷阱箱&r&e使用此命令！"
# 密码错误时的提示语
PassWordError: "&c密码错误，请检查 &e&l大小写/密码 &r&c正确性！"
# 密码正确的提示信息
PassWordCorrect: "&a密码正确，箱子已解锁！"

~~~

## 命令

* /steelsafe[ss] help　　　　　　　　　　-列出所有命令
* /steelsafe[ss] create 密码 重复密码　　-以你面向的箱子创建一个密码保险箱。
* /steelsafekey[ssk] 密码　　　　　　　　-以密码尝试打开上锁的保险箱 
* /steelsafeshowkey[sssk]　　　　　　　　 -列出你的所有的保险箱
* /steelsafereload[ssr]　　　　　　　　　-重载插件配置[管理员]
* /steelsafereremove[ssre]　　　　　　　-强制删除面前的保险箱[管理员]

