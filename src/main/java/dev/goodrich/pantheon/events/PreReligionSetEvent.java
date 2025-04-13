package dev.goodrich.pantheon.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PreReligionSetEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String religion;
    private final Player player;
    private boolean cancelled;
    private String cancelMessage = "Religion setting cancelled.";

    /**
     * Creates a new PreReligionSetEvent.
     *
     * @param religion the religion to be set
     * @param player   the player setting the religion
     */
    public PreReligionSetEvent(String religion, Player player) {
        this.religion = religion;
        this.player = player;
        this.cancelled = false;
    }

    /**
     * Gets the religion being set.
     *
     * @return the chosen religion
     */
    public String getReligion() {
        return religion;
    }

    /**
     * Gets the player attempting to set their religion.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the cancellation message.
     *
     * @return the message to display if the event is cancelled
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * Sets a custom cancellation message.
     *
     * @param cancelMessage the message to display if the event is cancelled
     */
    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Needed for compatibility with the Bukkit event system.
     *
     * @return the HandlerList for this event.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
