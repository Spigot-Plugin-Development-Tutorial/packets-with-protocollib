package me.kodysimpson.packetstutorialprotocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.kodysimpson.packetstutorialprotocollib.commands.BoomCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketsTutorialProtocolLib extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("boom").setExecutor(new BoomCommand());

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        //Listening to an Incoming packet - from the CLIENT to the SERVER
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();
                //Accessing the data of the packet as matched by the https://wiki.vg/Protocol
                double x = packet.getDoubles().read(0);
                double y = packet.getDoubles().read(1);
                double z = packet.getDoubles().read(2);
                boolean onGround = packet.getBooleans().read(0);
                p.sendMessage("INBOUND X: " + x + " Y: " + y + " Z: " + z + " : On Ground? " + onGround);
            }
        });

        //Listening to an Outgoing packet - from the SERVER to the CLIENT
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.REL_ENTITY_MOVE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                short x = packet.getShorts().read(0);
                short y = packet.getShorts().read(1);
                short z = packet.getShorts().read(2);
                System.out.println(x + " : " + y + " : " + z);
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
