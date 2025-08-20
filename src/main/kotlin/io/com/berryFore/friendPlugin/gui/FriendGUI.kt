package io.com.berryFore.friendPlugin.gui

import io.com.berryFore.friendPlugin.manager.FriendManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

object FriendGUI : Listener {
    fun openFriendGUI(player: Player) {
        val friends = FriendManager.getFriends(player.uniqueId)
        val size = 54 // 기본 크기를 54칸으로 고정
        val inventory = Bukkit.createInventory(null, size, Component.text("친구 목록"))

        friends.forEachIndexed { index, friendUUID ->
            val friend = Bukkit.getOfflinePlayer(friendUUID)
            val item = ItemStack(Material.PLAYER_HEAD)
            val meta = item.itemMeta as SkullMeta
            meta.owningPlayer = friend
            meta.displayName(Component.text(friend.name ?: "알 수 없는 플레이어"))
            item.itemMeta = meta
            inventory.setItem(index, item)
        }

        player.openInventory(inventory)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title() != Component.text("친구 목록")) return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        val slot = event.slot
        val friends = FriendManager.getFriends(player.uniqueId)
        if (slot >= friends.size) return

        val friendUUID = friends[slot]
        val friend = Bukkit.getOfflinePlayer(friendUUID)

        when (event.click) {
            org.bukkit.event.inventory.ClickType.LEFT -> {
                // 친구 정보 표시
                player.sendMessage(
                    Component.text("친구 정보: ${friend.name}", NamedTextColor.YELLOW)
                        .append(Component.text("\nUUID: $friendUUID"))
                        .append(Component.text("\n온라인: ${if (friend.isOnline) "예" else "아니오"}"))
                )
            }
            org.bukkit.event.inventory.ClickType.RIGHT -> {
                // 친구 삭제
                FriendManager.removeFriend(player.uniqueId, friendUUID)
                player.sendMessage(Component.text("${friend.name}을(를) 친구 목록에서 삭제했습니다.", NamedTextColor.RED))
                if (friend.isOnline) {
                    friend.player?.sendMessage(Component.text("${player.name}님이 친구 목록에서 당신을 삭제했습니다.", NamedTextColor.RED))
                }
                openFriendGUI(player) // GUI 갱신
            }
            else -> {}
        }
    }
}