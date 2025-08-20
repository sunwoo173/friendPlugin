package io.com.berryFore.friendPlugin.manager

import io.com.berryFore.friendPlugin.FriendPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.UUID

object FriendManager {
    private val friends = mutableMapOf<UUID, MutableList<UUID>>()
    private val requests = mutableMapOf<UUID, MutableList<UUID>>()
    private val file = File(FriendPlugin.instance.dataFolder, "friends.yml")
    private val config = YamlConfiguration()

    fun loadFriends() {
        if (!file.exists()) {
            FriendPlugin.instance.saveResource("friends.yml", false) // 기본 friends.yml 복사
        }
        config.load(file)
        config.getKeys(false).forEach { uuid ->
            val friendsList = config.getStringList(uuid).mapNotNull { it.toUUIDOrNull() }
            friends[UUID.fromString(uuid)] = friendsList.toMutableList()
        }
    }

    fun saveFriends() {
        friends.forEach { (uuid, friendsList) ->
            config.set(uuid.toString(), friendsList.map { it.toString() })
        }
        config.save(file)
    }

    fun addFriend(player: UUID, friend: UUID) {
        friends.getOrPut(player) { mutableListOf() }.add(friend)
        friends.getOrPut(friend) { mutableListOf() }.add(player)
    }

    fun removeFriend(player: UUID, friend: UUID) {
        friends[player]?.remove(friend)
        friends[friend]?.remove(player)
    }

    fun addRequest(sender: UUID, receiver: UUID) {
        requests.getOrPut(receiver) { mutableListOf() }.add(sender)
    }

    fun removeRequest(sender: UUID, receiver: UUID) {
        requests[receiver]?.remove(sender)
    }

    fun getFriends(player: UUID): List<UUID> = friends[player] ?: emptyList()
    fun getRequests(player: UUID): List<UUID> = requests[player] ?: emptyList()

    private fun String.toUUIDOrNull(): UUID? = try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}