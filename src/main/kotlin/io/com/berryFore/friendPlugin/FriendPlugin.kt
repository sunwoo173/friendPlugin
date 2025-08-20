package io.com.berryFore.friendPlugin

import io.com.berryFore.friendPlugin.command.FriendCommand
import io.com.berryFore.friendPlugin.command.RequestCommand
import io.com.berryFore.friendPlugin.gui.FriendGUI
import io.com.berryFore.friendPlugin.listener.ClickListener
import io.com.berryFore.friendPlugin.listener.ConnectionListener
import io.com.berryFore.friendPlugin.manager.FriendManager
import org.bukkit.plugin.java.JavaPlugin

class FriendPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: FriendPlugin
            private set
    }

    override fun onEnable() {
        instance = this
        FriendManager.loadFriends() // 친구 데이터 로드
        getCommand("친구")?.setExecutor(FriendCommand())
        getCommand("친구신청")?.setExecutor(RequestCommand())
        server.pluginManager.registerEvents(ClickListener(), this)
        server.pluginManager.registerEvents(FriendGUI, this)
        server.pluginManager.registerEvents(ConnectionListener(), this) // JoinListener 등록
    }

    override fun onDisable() {
        FriendManager.saveFriends() // 친구 데이터 저장
    }
}