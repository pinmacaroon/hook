# TODO.md

This file is basically a checklist of things to do in the codebase.

## Chores

* in `class util.PromotionProvider` make promotions more normal, make jsonobjects static stuff in the class 
* refactor method `util.PromotionProvider.sendAutomaticPromotion` to `sendPromotion` and maybe merge it with `automaticPromotionSelector`
* refactor method `util.PromotionProvider.sendPromotion` to `sendPromotionMessageAPIRequest`
* in class `class util.EventListeners` make stuff not lambdas because its messy
* in class `class util.EventListeners` check for `ModConfigs.MESSAGES_X_ALLOWED` before registering event to prevent unnecessary event registration
    * dont forget to register `ServerLifecycleEvents.SERVER_STOPPED` in all cases as the `if(Hook.BOT != null) Hook.BOT.stop();` must run even if the messages arent allowed
* seperate many functions of the `ServerMessageEvents.CHAT_MESSAGE` method
* parse timestamp message
* figure out the black magic of `ServerMessageEvents.GAME_MESSAGE` (?)

## Implement

* Performance improvements, don't know how yet
