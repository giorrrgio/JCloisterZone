package com.jcloisterzone.ui.controls;

import java.awt.Color;
import java.awt.Image;
import java.util.Collection;
import java.util.Map;

import sun.net.www.content.text.plain;

import com.google.common.collect.Maps;
import com.jcloisterzone.Player;
import com.jcloisterzone.board.Tile;
import com.jcloisterzone.figure.Meeple;
import com.jcloisterzone.game.expansion.AbbeyAndMayorGame;
import com.jcloisterzone.game.expansion.BridgesCastlesBazaarsGame;
import com.jcloisterzone.game.expansion.KingAndScoutGame;
import com.jcloisterzone.game.expansion.TowerGame;
import com.jcloisterzone.game.expansion.TradersAndBuildersGame;
import com.jcloisterzone.ui.Client;
import com.jcloisterzone.ui.theme.FigureTheme;

public class PlayerPanelImageCache {

    private final Client client;
    private Map<String, Image> scaledImages = Maps.newHashMap();

    public PlayerPanelImageCache(Client client) {
        this.client = client;
        scaleImages();
    }

    public Image get(Player player, String key) {
        if (player == null) {
            return scaledImages.get(key);
        } else {
            return scaledImages.get(player.getIndex() + key);
        }
    }

    private Image scaleImage(Image img) {
        return img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    }

    private void scaleFigureImages(Player player, Color color, Collection<? extends Meeple> meeples) {
        FigureTheme theme = client.getFigureTheme();
        //Image img = theme.getFigureImage(type, color, null);
        for(Meeple f : meeples) {
            String key = player.getIndex() + f.getClass().getSimpleName();
            if (!scaledImages.containsKey(key)) {
                scaledImages.put(key, scaleImage(theme.getFigureImage(f.getClass(), color, null)));
            }
        }
    }

    private void scaleImages() {
        FigureTheme theme = client.getFigureTheme();
        for(Player player : client.getGame().getAllPlayers()) {
            Color color = client.getPlayerColor(player);
            scaleFigureImages(player, color, player.getFollowers());
            scaleFigureImages(player, color, player.getSpecialMeeples());
        }
        TowerGame tower = client.getGame().getTowerGame();
        if (tower != null) {
            scaledImages.put("towerpiece", scaleImage(theme.getNeutralImage("towerpiece")));
        }
        KingAndScoutGame ks = client.getGame().getKingAndScoutGame();
        if (ks != null) {
            scaledImages.put("king", scaleImage(theme.getNeutralImage("king")));
            scaledImages.put("robber", scaleImage(theme.getNeutralImage("robber")));
        }
        BridgesCastlesBazaarsGame bcb = client.getGame().getBridgesCastlesBazaarsGame();
        if (bcb != null) {
            scaledImages.put("bridge", scaleImage(theme.getNeutralImage("bridge")));
            scaledImages.put("castle", scaleImage(theme.getNeutralImage("castle")));
        }
        TradersAndBuildersGame tb = client.getGame().getTradersAndBuildersGame();
        if (tb != null) {
            scaledImages.put("cloth", scaleImage(theme.getNeutralImage("cloth")));
            scaledImages.put("grain", scaleImage(theme.getNeutralImage("grain")));
            scaledImages.put("wine", scaleImage(theme.getNeutralImage("wine")));
        }
        AbbeyAndMayorGame ab = client.getGame().getAbbeyAndMayorGame();
        if (ab != null) {
            scaledImages.put("abbey", scaleImage(client.getTileTheme().getTileImage(Tile.ABBEY_TILE_ID)));
        }
    }

}
