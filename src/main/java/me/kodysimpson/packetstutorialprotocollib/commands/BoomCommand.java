package me.kodysimpson.packetstutorialprotocollib.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class BoomCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            Player p = (Player) sender;

            ProtocolManager manager = ProtocolLibrary.getProtocolManager();

            p.getLineOfSight(null, 50).stream()
                    .filter(block -> block.getType() != Material.AIR)
                    .forEach(block -> {

                        Location blockLocation = block.getLocation();

                        //Construct a packet that we can send to someone
                        //The PacketType will be Server if its origin is the Server(clientbound), Client if its origin is the Client(serverbound)
                        PacketContainer packet = manager.createPacket(PacketType.Play.Server.EXPLOSION);

                        //Write the data of the packet in accordance with https://wiki.vg/Protocol
                        packet.getDoubles().write(0, blockLocation.getX());
                        packet.getDoubles().write(1, blockLocation.getY());
                        packet.getDoubles().write(2, blockLocation.getZ());

                        packet.getFloat().write(0, 0.5f); //strength
                        packet.getFloat().write(1, 0.0f); //x velocity
                        packet.getFloat().write(2, 0.5f); //y velocity
                        packet.getFloat().write(3, 5.0f); //z velocity

                        try {
                            //send to a single player so only they see the explosion happen
                            manager.sendServerPacket(p, packet);
                            //send to all players
                            //manager.broadcastServerPacket(packet);
                        } catch (InvocationTargetException e) {
                            System.out.println("oopsies");
                        }
                    });


        }

        return true;
    }
}
