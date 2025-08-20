package io.com.berryFore.friendPlugin.listener

import io.com.berryFore.friendPlugin.manager.FriendManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.UUID

class ClickListener : Listener {
    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message.split(" ")

        if (message.size != 3 || message[0] != "/friendplugin") return

        val action = message[1]
        val senderUUID = try {
            UUID.fromString(message[2])
        } catch (e: IllegalArgumentException) {
            return
        }

        val sender = Bukkit.getPlayer(senderUUID) ?: return
        val requests = FriendManager.getRequests(player.uniqueId)

        if (!requests.contains(senderUUID)) {
            player.sendMessage(Component.text("유효하지 않은 요청입니다.", NamedTextColor.RED))
            return
        }

        when (action) {
            "accept" -> {
                FriendManager.addFriend(player.uniqueId, senderUUID)
                FriendManager.removeRequest(senderUUID, player.uniqueId)
                player.sendMessage(Component.text("${sender.name}와 친구가 되었습니다.", NamedTextColor.GREEN))
                sender.sendMessage(Component.text("${player.name}님이 친구 요청을 수락했습니다.", NamedTextColor.GREEN))
            }
            "reject" -> {
                FriendManager.removeRequest(senderUUID, player.uniqueId)
                player.sendMessage(Component.text("${sender.name}의 친구 요청을 거절했습니다.", NamedTextColor.RED))
                sender.sendMessage(Component.text("${player.name}님이 친구 요청을 거절했습니다.", NamedTextColor.RED))
            }
        }

        event.isCancelled = true
    }
}