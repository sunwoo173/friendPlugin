package io.com.berryFore.friendPlugin.listener

import io.com.berryFore.friendPlugin.manager.FriendManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val friends = FriendManager.getFriends(player.uniqueId)

        friends.forEach { friendUUID ->
            val friend = Bukkit.getPlayer(friendUUID)
            if (friend != null && friend.isOnline) {
                friend.sendMessage(
                    Component.text("[친구] ", NamedTextColor.GREEN)
                        .append(Component.text("${player.name}님이 접속하였습니다.", NamedTextColor.YELLOW))
                )
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val friends = FriendManager.getFriends(player.uniqueId)

        friends.forEach { friendUUID ->
            val friend = Bukkit.getPlayer(friendUUID)
            if (friend != null && friend.isOnline) {
                friend.sendMessage(
                    Component.text("[친구] ", NamedTextColor.GREEN)
                        .append(Component.text("${player.name}님이 퇴장하였습니다.", NamedTextColor.YELLOW))
                )
            }
        }
    }
}