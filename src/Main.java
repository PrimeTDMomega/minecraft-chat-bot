// Go here - https://raw.githubusercontent.com/WitheredKnights/resourcePack-Tutorial/main/pack.png




import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Predicate;


public class MyMinecraftBot {
    public static void main(String[] args) {
        // Connect to the server and join a game
        Minecraft mc = Minecraft.getMinecraft();

        // Continuously check for new chat messages
        while (true) {
            // Check if a chat message was received
            if (mc.ingameGUI.getChatGUI().getChatOpen()) {
                // Get the latest chat message and the username of the sender
                String chatMessage = mc.ingameGUI.getChatGUI().getChatMessage();
                String sender = mc.ingameGUI.getChatGUI().getSentMessageName();

                // Check if the chat message starts with "!find"
                if (chatMessage.startsWith("!find")) {
                    // Find the player with the given username
                    EntityPlayer target = mc.theWorld.getPlayerEntityByName(sender);

                    // Check if the player was found
                    if (target != null) {
                        // Set the bot's target to the player's coordinates
                        mc.thePlayer.setPositionAndUpdate(target.posX, target.posY, target.posZ);

                        // Send a message back to the chat indicating that the bot is following the player
                        String followMessage = "I am now following " + sender + "!";
                        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + followMessage));
                    } else {
                        // Send a message back to the chat indicating that the player was not found
                        String notFoundMessage = "Could not find player " + sender + "!";
                        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + notFoundMessage));
                    }
                }

                // Check if the chat message starts with "!coords"
                if (chatMessage.startsWith("!coords")) {
                    // Get the bot's current coordinates
                    double x = mc.thePlayer.posX;
                    double y = mc.thePlayer.posY;
                    double z = mc.thePlayer.posZ;

                    // Send a message back to the chat with the bot's current coordinates
                    String coordsMessage = "I am currently at (" + x + ", " + y + ", " + z + ")";
                    mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + coordsMessage));
                }

                // Check if the chat message starts with "!tpa"
                if (chatMessage.startsWith("!tpa")) {
                    String[] messageParts = chatMessage.split(" ");
                    if (messageParts.length >= 2) {
                        String targetName = messageParts[1];
                        mc.thePlayer.sendChatMessage("/tpa " + targetName);
                    }
                }

                // Sleep for a short time before checking for new chat messages again
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Do nothing
                }
                if (chatMessage.startsWith("!hunt")) {
                    // Find all nearby mobs of the specified types
                    List<EntityLivingBase> mobs = mc.theWorld.getEntitiesWithinAABB(EntityLivingBase.class, mc.thePlayer.getEntityBoundingBox().expand(10, 10, 10), new Predicate<EntityLivingBase>() {
                        @Override
                        public boolean apply(EntityLivingBase entity) {
                            return (entity instanceof EntityCow || entity instanceof EntityPig || entity instanceof EntitySheep);
                        }
                    });

                    // Attack and kill each mob
                    for (EntityLivingBase mob : mobs) {
                        mc.thePlayer.attackTargetEntityWithCurrentItem(mob);
                        mc.thePlayer.swingItem();
                    }

                    // Collect the drops from the killed mobs
                    for (EntityLivingBase mob : mobs) {
                        for (EntityItem item : mob.capturedDrops) {
                            mc.thePlayer.addToPlayerInventory(item.getEntityItem());
                        }
                    }
                }
            }
        }
    }