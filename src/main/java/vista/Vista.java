package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Pedro Bennassar
 */
public class Vista {

    static private TreeMap<Integer, String> tasks = null; //El mapa se ordena según el entero con formato aaammddn (n: numero de tarea diaria)
    static LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
    static JFrame contentFrame = new JFrame(); //frame principal
    static JPanel midPanel = new JPanel(), topPanel = new JPanel(), bottomPanel = new JPanel();//panel con los días del mes. Cada día es un botón para poder acceder a las tareas de ese día
    static JButton prevMonth = new JButton("<<"), nextMonth = new JButton(">>"), prevYear = new JButton("<<"), nextYear = new JButton(">>"), addTask = new JButton("Añadir tarea"), sendData = new JButton("Enviar datos"), viewTasks = new JButton("Ver tareas del mes");
    static JLabel monthLabel = new JLabel(transformMonth(date.getMonthValue()), SwingConstants.CENTER), yearLabel = new JLabel("" + date.getYear(), SwingConstants.CENTER), infoLabel = new JLabel(), bottomLabel = new JLabel();
    static JDialog dialogTask = new JDialog(contentFrame, "Añadir tarea");
    static JTextField tfDay = new JTextField("Día"), tfMonth = new JTextField("Mes"), tfYear = new JTextField("Año"), tfTime = new JTextField("Hora");
    static JTextArea taDescription = new JTextArea("Descripción", 5, 40);
    static final String[] DIAS_SEMANA = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    static private String diaActual = "";

    public Vista() throws IOException {

        //leer archivo de tareas
        readMap();
//        System.out.println(tasks.keySet());
        Vista.setLayouts();
        Vista.addComps();
        Vista.addListeners();
        Vista.setOptions();
    }

    public static void readMap() throws IOException {
        FileInputStream f = null;
        ObjectInputStream ois = null;

        try {
            f = new FileInputStream("mapa.dat");
            ois = new ObjectInputStream(f);
            Vista.tasks = (TreeMap) ois.readObject();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            f.close();
            ois.close();
        }
    }

    public static int transformDay(String dia) {
        switch (dia) {
            case "MONDAY":
                return 1;
            case "TUESDAY":
                return 2;
            case "WEDNESDAY":
                return 3;
            case "THURSDAY":
                return 4;
            case "FRIDAY":
                return 5;
            case "SATURDAY":
                return 6;
            case "SUNDAY":
                return 7;
            default:
                return 0;
        }

    }

    public static String transformMonth(int mes) {
        switch (mes) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "0";
        }
    }

    public static int transformMonth(String mes) {
        switch (mes) {
            case "Enero":
                return 1;
            case "Febrero":
                return 2;
            case "Marzo":
                return 3;
            case "Abril":
                return 4;
            case "Mayo":
                return 5;
            case "Junio":
                return 6;
            case "Julio":
                return 7;
            case "Agosto":
                return 8;
            case "Septiembre":
                return 9;
            case "Octubre":
                return 10;
            case "Noviembre":
                return 11;
            case "Diciembre":
                return 12;
            default:
                return 0;
        }
    }

    public static void addDays(JPanel panel, int primerDia, int ultimoDia, JFrame f, String month, String year) {
        for (int i = 1; i <= 42; i++) {
            if (i >= primerDia && i < ultimoDia + primerDia) {
                JButton aux = new JButton();
                aux.setText("" + (i - primerDia + 1));
                aux.setFont(new Font("Serif", Font.PLAIN, 16));
                aux.setName(yearLabel.getText() + transformMonth(monthLabel.getText()) + aux.getText());

//VER SI ESTE DIA TIENE TAREAS
//SI TIENE TAREAS, SE PINTA DE AZUL
                String dia = yearLabel.getText() + transformMonth(monthLabel.getText()) + aux.getText();
                if (tasks != null) {

                    for (int key : tasks.keySet()) {
                        int auxKey = (key - key % 10) / 10;
                        if (("" + auxKey).equals(dia)) {
                            aux.setBackground(Color.cyan);
                            aux.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    //VER TAREAS DEL DÍA
                                    String str = "<html>";
                                    String dia = yearLabel.getText() + transformMonth(monthLabel.getText()) + aux.getText();
                                    for (int key : tasks.keySet()) {
                                        if (("" + ((key - key % 10) / 10)).equals(dia)) {
                                            str += tasks.get(key) + "<br>";
                                        }
                                    }
                                    str += "</html>";
                                    bottomLabel.setText(str);
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                    //OCULTAR TAREAS DEL DÍA
                                    //ACTUALIZAR LABEL DEL BOTTOM A ""
                                    bottomLabel.setText("");
                                }
                            });
                            break;
                        }

                    }
                }
                aux.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //ABRIR DIÁLOGO PARA INTRODUCIR DATOS DE LA NUEVA TAREA

                        //Añadir número de tarea
                        //Comprobar si ese día hay más tareas
                        int tareas = 0;
                        if (tasks != null) {
                            for (int key : tasks.keySet()) {
                                int auxKey = (key - key % 10) / 10;
                                if (("" + auxKey).equals(dia)) {
                                    tareas++;
                                }
                            }
                        }

                        dialogTask.setVisible(true);

                        diaActual = yearLabel.getText() + transformMonth(monthLabel.getText()) + aux.getText() + (tareas + 1);

                        //Si no hay, empieza en 1
                        //Si hay se incrementa la cantidad de tareas
                    }
                });
                //CUANDO SE PASA EL RATÓN POR ENCIMA DE UN BOTÓN SIN HACER CLICK, SE MUESTRAN LAS TAREAS EN LA PARTE INFERIOR DEL BORDER PANEL
                panel.add(aux);
            } else {
                panel.add(new JLabel());
            }
        }
        panel.repaint();
    }

    public static void addDaysOfWeek(JPanel panel) {
        for (int i = 0; i < 7; i++) {
            JLabel aux = new JLabel(DIAS_SEMANA[i], SwingConstants.CENTER);
            aux.setFont(new Font("Serif", Font.PLAIN, 16));
            panel.add(aux);
        }
    }

    public static void setLayouts() {
        contentFrame.setLayout(
                new BorderLayout());
        midPanel.setLayout(
                new GridLayout(0, 7));
        topPanel.setLayout(
                new GridLayout(1, 6));
        bottomPanel.setLayout(new FlowLayout());
        dialogTask.setLayout(new FlowLayout());
    }

    public static void addComps() {
        //MidPanel
        addDaysOfWeek(midPanel);
        addDays(midPanel, transformDay(date.getDayOfWeek().toString()), date.lengthOfMonth(), contentFrame, monthLabel.getText(), yearLabel.getText());//Añadimos el mes actual
        contentFrame.add(midPanel, BorderLayout.CENTER);
        //TopPanel
        topPanel.add(prevMonth);
        topPanel.add(monthLabel);
        topPanel.add(nextMonth);
        topPanel.add(prevYear);
        topPanel.add(yearLabel);
        topPanel.add(nextYear);
        contentFrame.add(topPanel, BorderLayout.NORTH);
        //BottomPanel
        bottomPanel.add(bottomLabel);
        contentFrame.add(bottomPanel, BorderLayout.SOUTH);
        //dialogTask
        //SE TIENE QUE ABRIR AL PULSAR EN EL DÍA
//        dialogTask.add(tfDay);
//        dialogTask.add(tfMonth);
//        dialogTask.add(tfYear);
//        dialogTask.add(tfTime);
        dialogTask.add(taDescription);
        dialogTask.add(sendData);
        dialogTask.setSize(600, 200);
    }

    public static void addListeners() {
        //Oyentes
        prevMonth.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.of(Integer.parseInt(yearLabel.getText()), transformMonth(monthLabel.getText()), 1); //Trabajamos con el día 1 del mes y año especificados en las etiquetas del programa
                date = date.minusMonths(1);
                monthLabel.setText(transformMonth(date.getMonthValue()));
                midPanel.removeAll();
                addDaysOfWeek(midPanel);
                int diaEntero = transformDay(date.getDayOfWeek().toString());
                if (date.getMonthValue() == 12) {
                    yearLabel.setText("" + (Integer.parseInt(yearLabel.getText()) - 1));
                }
                addDays(midPanel, diaEntero, date.lengthOfMonth(), contentFrame, monthLabel.getText(), yearLabel.getText());
            }
        }
        );
        nextMonth.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.of(Integer.parseInt(yearLabel.getText()), transformMonth(monthLabel.getText()), 1); //Trabajamos con el día 1 del mes y año especificados en las etiquetas del programa
                date = date.plusMonths(1);//
                monthLabel.setText(transformMonth(date.getMonthValue()));
                midPanel.removeAll();
                if (date.getMonthValue() == 1) {
                    yearLabel.setText("" + (Integer.parseInt(yearLabel.getText()) + 1));
                }
                addDaysOfWeek(midPanel);
                int diaEntero = transformDay(date.getDayOfWeek().toString());
                addDays(midPanel, diaEntero, date.lengthOfMonth(), contentFrame, monthLabel.getText(), yearLabel.getText());
//                midPanel.repaint();
            }
        }
        );
        prevYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.of(Integer.parseInt(yearLabel.getText()), transformMonth(monthLabel.getText()), 1); //Trabajamos con el día 1 del mes y año especificados en las etiquetas del programa
                date = date.minusYears(1);//
                yearLabel.setText("" + (Integer.parseInt(yearLabel.getText()) - 1));
                midPanel.removeAll();
                addDaysOfWeek(midPanel);
                int diaEntero = transformDay(date.minusDays(date.getDayOfMonth() - 1).getDayOfWeek().toString()); //Número que identifica el día de la semana en el que empieza el mes
                addDays(midPanel, diaEntero, date.lengthOfMonth(), contentFrame, monthLabel.getText(), yearLabel.getText());
//                midPanel.repaint();
            }
        });
        nextYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.of(Integer.parseInt(yearLabel.getText()), transformMonth(monthLabel.getText()), 1); //Trabajamos con el día 1 del mes y año especificados en las etiquetas del programa
                date = date.plusYears(1);//
                yearLabel.setText("" + (Integer.parseInt(yearLabel.getText()) + 1));
                midPanel.removeAll();
                addDaysOfWeek(midPanel);
                int diaEntero = transformDay(date.minusDays(date.getDayOfMonth() - 1).getDayOfWeek().toString()); //Número que identifica el día de la semana en el que empieza el mes
                addDays(midPanel, diaEntero, date.lengthOfMonth(), contentFrame, monthLabel.getText(), yearLabel.getText());
//                midPanel.repaint();
            }
        });

        sendData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tasks.put(Integer.parseInt(diaActual), taDescription.getText());
                int diaActualNumero = Integer.parseInt(diaActual);
                dialogTask.setVisible(false);
                String diaTarea = "" + ((diaActualNumero - diaActualNumero % 10) / 10);
                for (Component c : midPanel.getComponents()) {
                    if (c.getName() != null) {
//                        System.out.println("diaTarea = " + diaTarea);
                        if (c.getName().equals(diaTarea)) {
                            //
                            c.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    //VER TAREAS DEL DÍA
                                    String str = "<html>";
                                    String dia = yearLabel.getText() + transformMonth(monthLabel.getText()) + c.getName();
                                    for (int key : tasks.keySet()) {
                                        if (("" + ((key - key % 10) / 10)).equals(c.getName())) {
                                            str += tasks.get(key) + "<br>";
                                        }
                                    }
                                    str += "</html>";
                                    bottomLabel.setText(str);
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                    bottomLabel.setText("");
                                }
                            });
                            c.setBackground(Color.cyan);
                            midPanel.repaint();
                        }

                    }

                }

            }
        });

        contentFrame.addWindowListener( new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //Guardar mapa
                FileOutputStream f = null;
                ObjectOutputStream oos = null;

                try {
                    f = new FileOutputStream("mapa.dat");
                    oos = new ObjectOutputStream(f);
                    
                    oos.writeObject(tasks);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                } finally {
                    try {
                        f.close();
                        oos.close();
                    } catch (IOException ex) {
                    }
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public static void setOptions() {

        contentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentFrame.setResizable(
                false);
        contentFrame.setSize(
                700, 700);
        contentFrame.setVisible(
                true);
    }

}
