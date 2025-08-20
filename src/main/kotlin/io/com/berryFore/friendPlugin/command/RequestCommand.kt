package io.com.berryFore.friendPlugin.command

import io.com.berryFore.friendPlugin.manager.FriendManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RequestCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다.", NamedTextColor.RED))
            return true
        }

        if (args.size != 1) {
            sender.sendMessage(Component.text("사용법: /친구신청 <닉네임>", NamedTextColor.RED))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null || !target.isOnline) {
            sender.sendMessage(Component.text("플레이어가 온라인 상태가 아닙니다.", NamedTextColor.RED))
            return true
        }

        if (target == sender) {
            sender.sendMessage(Component.text("자신에게 친구 요청을 보낼 수 없습니다.", NamedTextColor.RED))
            return true
        }

        if (FriendManager.getFriends(sender.uniqueId).contains(target.uniqueId)) {
            sender.sendMessage(Component.text("이미 친구입니다.", NamedTextColor.RED))
            return true
        }

        FriendManager.addRequest(sender.uniqueId, target.uniqueId)
        sender.sendMessage(Component.text("${target.name}에게 친구 요청을 보냈습니다.", NamedTextColor.GREEN))

        val accept = Component.text("[수락]").color(NamedTextColor.GREEN)
            .clickEvent(ClickEvent.runCommand("/friendplugin accept ${sender.uniqueId}"))
        val reject = Component.text("[거절]").color(NamedTextColor.RED)
            .clickEvent(ClickEvent.runCommand("/friendplugin reject ${sender.uniqueId}"))
        target.sendMessage(
            Component.text("${sender.name}님의 친구 요청: ").color(NamedTextColor.YELLOW)
                .append(accept)
                .append(Component.text(" "))
                .append(reject)
        )

        return true
    }
}