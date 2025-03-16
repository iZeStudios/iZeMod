package net.izestudios.izemod.injection.mixin;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        for (Element element : this.children()) {
            if (element instanceof ButtonWidget widget
                && widget.getMessage().getContent() instanceof TranslatableTextContent ttc) {

                String key = ttc.getKey();

                switch (key) {
                    case "selectServer.select":
                        widget.setX(this.width / 2 - 206);
                        widget.setY(this.height - 52);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "selectServer.direct":
                        widget.setX(this.width / 2 - 102);
                        widget.setY(this.height - 52);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "selectServer.add":
                        widget.setX(this.width / 2 + 2);
                        widget.setY(this.height - 52);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "selectServer.edit":
                        widget.setX(this.width / 2 - 206);
                        widget.setY(this.height - 28);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "selectServer.delete":
                        widget.setX(this.width / 2 - 102);
                        widget.setY(this.height - 28);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "selectServer.refresh":
                        widget.setX(this.width / 2 + 2);
                        widget.setY(this.height - 28);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    case "gui.cancel":
                    case "gui.back":
                    case "menu.returnToMenu":
                        widget.setX(this.width / 2 + 4 + 102);
                        widget.setY(this.height - 28);
                        widget.setWidth(100);
                        widget.setHeight(20);
                        break;
                    default:
                }
            }
        }

        this.addDrawableChild(
            ButtonWidget.builder(Text.translatable("screens.multiplayer.manage"), button -> {})
                .dimensions(this.width / 2 + 4 + 102, this.height - 52, 100, 20)
                .build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(Text.translatable("screens.multiplayer.last"), button -> {})
                .dimensions(this.width - 207 , 5, 100, 20)
                .build()
        );
    }
}
