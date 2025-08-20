package io.com.berryFore.friendPlugin.command

import io.com.berryFore.friendPlugin.gui.FriendGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FriendCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.")
            return true
        }

        FriendGUI.openFriendGUI(sender)
        return true
    }
}