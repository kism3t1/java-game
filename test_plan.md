Test Plans for Player Armour:

	Test objective: Verify that player armour bar decreases before player health bar.
	Technique: Start Game. 
	Walk into pool with many floating eyeballs. 
	Observe armour bar and health bar while enemies attack.
	Armour bar should decrease to zero, only then health bar should decrease.
	Result: Test success.

Test Plans for Player Health:

	Test objective: Verify that player health bar decreases when hit by enemies.
	Technique: Start Game. 
	Walk into pool with many floating eyeballs. 
	Wait for armour bar to be destroyed. 
	Observe health bar while enemies attack.
	Result: Test success.

	Test objective: Verify that when player health bar empty, game ends.
	Technique: Start Game. Walk into pool with many floating eyeballs. 
	Wait for armour bar to be destroyed. 
	Wait for health bar to empty. 
	Result: Test failed. 
	Notes: After health bar reaches empty stage, the red bar wraps around the other way and grows.