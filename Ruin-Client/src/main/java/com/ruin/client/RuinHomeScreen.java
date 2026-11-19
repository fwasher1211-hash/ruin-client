package com.ruin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

/** Custom launcher-style landing screen. Open it via HOME in the Ruin GUI. */
public final class RuinHomeScreen extends Screen {
    public RuinHomeScreen() { super(Text.literal("Ruin Client Home")); }
    @Override public void render(DrawContext ctx,int mouseX,int mouseY,float delta) {
        super.render(ctx,mouseX,mouseY,delta);
        ctx.fill(0,0,width,height,0xFF090611);
        int mid=width/2, top=Math.max(20,height/2-110);
        ctx.fill(mid-150,top-22,mid+150,top+220,0xEF191124);
        ctx.fill(mid-150,top-22,mid+150,top-19,RuinClient.accent());
        ctx.drawCenteredTextWithShadow(textRenderer,"R U I N   C L I E N T",mid,top, RuinClient.accent());
        ctx.drawCenteredTextWithShadow(textRenderer,"MINECRAFT 1.21.11  |  v0.1.0",mid,top+20,0xFFC0B2D0);
        String[] labels={"Minecraft Main Menu","Ruin Modules","Themes","Close"};
        for(int i=0;i<labels.length;i++){
            int yy=top+48+i*39;
            ctx.fill(mid-115,yy,mid+115,yy+30,0xFF2A1A39);
            ctx.fill(mid-115,yy,mid-112,yy+30,RuinClient.accent());
            ctx.drawCenteredTextWithShadow(textRenderer,labels[i],mid,yy+11,0xFFF5EEFF);
        }
    }
    @Override public boolean mouseClicked(double mx,double my,int button){
        int mid=width/2,top=Math.max(20,height/2-110);
        for(int i=0;i<4;i++){
            int yy=top+48+i*39;
            if(mx>=mid-115&&mx<=mid+115&&my>=yy&&my<=yy+30){
                switch(i){case 0 -> client.setScreen(new TitleScreen());case 1,2 -> client.setScreen(new RuinScreen());case 3 -> close();}
                return true;
            }
        }
        return super.mouseClicked(mx,my,button);
    }
}
