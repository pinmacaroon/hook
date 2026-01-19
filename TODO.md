# TODO.md

This file is basically a checklist of things to do in the codebase.

## Chores

* Add comments
* fix possible race condition so that the bot doesn't start with nonexistent ids
* seperate botless and botted startup code
* maybe add a "layer" for easier update to newer versions
* in class `class util.EventListeners` make stuff not lambdas because its messy
* in class `class util.EventListeners` check for `ModConfigs.MESSAGES_X_ALLOWED` before registering event to prevent unnecessary event registration
    * dont forget to register `ServerLifecycleEvents.SERVER_STOPPED` in all cases as the `if(Hook.BOT != null) Hook.BOT.stop();` must run even if the messages arent allowed
* seperate many functions of the `ServerMessageEvents.CHAT_MESSAGE` method
* figure out the black magic of `ServerMessageEvents.GAME_MESSAGE` (?)
* in `class util.PromotionProvider` make promotions more normal, load embed jsonobjects from json files n stuff for easy modification
* refactor method `util.PromotionProvider.sendPromotion` to `sendPromotionMessageAPIRequest`
* refactor method `util.PromotionProvider.sendAutomaticPromotion` to `sendPromotion` and maybe merge it with `automaticPromotionSelector`
* make a util class for method `util.WaypointParser` and use it in waypoint messages (maybe make them send an embed idk)

## Implement

* Performance improvements, don't know how yet
