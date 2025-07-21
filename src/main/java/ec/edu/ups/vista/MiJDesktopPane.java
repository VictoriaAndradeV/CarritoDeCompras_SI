package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
/**
 * Extensión de JDesktopPane que personaliza el fondo y dibuja un encabezado
 * estilizado con texto y subtítulo, brindando una apariencia de marca.
 * <p>
 * Pinta un fondo gris claro y renderiza el título "HICKYS" con estilo de fuente grande,
 * alternando colores para las letras inicial y final, y un subtítulo "Market Place"
 * centrado debajo del título principal.
 * </p>
 */
public class MiJDesktopPane extends JDesktopPane {

    public MiJDesktopPane() {
        setBackground(new Color(240, 240, 240)); // fondo gris claro
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Fondo gris claro
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());

        String texto = "HICKYS";
        char[] letras = texto.toCharArray();

        //tipo de letra para hickys
        Font fuenteTitulo = new Font("Arial", Font.BOLD, 200);
        g.setFont(fuenteTitulo);
        FontMetrics fm = g.getFontMetrics();

        //posicion horizontal total
        int anchoTotal = 0;
        for (char c : letras) {
            anchoTotal += fm.charWidth(c);
        }

        int x = (getWidth() - anchoTotal) / 2;
        int y = getHeight() / 3;

        // Colores definidos
        Color azulOscuro = new Color(0, 45, 100);
        Color turquesa = new Color(0, 200, 200);

        // Dibujar letras una por una con color específico
        for (int i = 0; i < letras.length; i++) {
            char letra = letras[i];
            if (i == 0 || i == letras.length - 1) {
                g.setColor(azulOscuro); //letras H y S
            } else {
                g.setColor(turquesa);//configurar letras ICKY
            }
            g.drawString(String.valueOf(letra), x, y);
            x += fm.charWidth(letra);
        }

        String subtitulo = "Market Place";
        Font fuenteSub = new Font("Arial", Font.PLAIN, 80);
        g.setFont(fuenteSub);
        g.setColor(new Color(0, 80, 160)); //azul

        FontMetrics fmSub = g.getFontMetrics();
        int xSub = (getWidth() - fmSub.stringWidth(subtitulo)) / 2;
        int ySub = y + fmSub.getHeight() + 40;

        g.drawString(subtitulo, xSub, ySub);
    }
}
