package com.clockblocker;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AmbientSoundEffect;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Iterator;

@Slf4j
@PluginDescriptor(
	name = "Clock Blocker",
	description = "Silence the grandfather clocks once and for all.",
	tags = {"sound", "clock", "mute", "grandfather"}
)
public class ClockBlockerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Clock Blocker started!");
		clientThread.invoke(() -> {
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				client.setGameState(GameState.LOADING);
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Clock Blocker stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			Iterator<AmbientSoundEffect> ambientSounds = client.getAmbientSoundEffects().iterator();
			while(ambientSounds.hasNext()) {
				AmbientSoundEffect sound = ambientSounds.next();
				if(sound.getSoundEffectId() == 3120) {
					ambientSounds.remove();
				}
			}
		}
	}
}
