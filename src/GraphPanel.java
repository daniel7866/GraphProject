import Graph.Edge;
import Graph.Exceptions.VertexNotExistException;
import Graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel implements ActionListener {
    private ArrayList<Edge<PaintingNode>> path = new ArrayList<Edge<PaintingNode>>();
    private ArrayList<PaintingNode> nodes = new ArrayList<>();
    private Graph<PaintingNode> graph = new Graph<PaintingNode>();


    private JButton btnAddNode=new JButton("Add Node"),btnAddEdge=new JButton("Add Edge"),btnBfs=new JButton("BFS"),btnDfs=new JButton("DFS"),
            btnClearPath=new JButton("Clear Path"),btnSave=new JButton("Save"),btnOpen=new JButton("Open");
    private JPanel buttonPanel=new JPanel(),filePanel=new JPanel();

    private PaintingNode prev = null, curr = null, selected = null;

    private Timer timer;
    private static final int DELAY = 1000;
    private int pathCnt = 0;

    private boolean flgAddNode = false, flgAddEdge = false;

    public GraphPanel(){
        setLayout(new BorderLayout());
        //add to main panel
        add(buttonPanel,BorderLayout.SOUTH);
        add(filePanel,BorderLayout.NORTH);
        buttonPanel.add(btnAddNode);
        buttonPanel.add(btnAddEdge);
        buttonPanel.add(btnBfs);
        buttonPanel.add(btnDfs);
        buttonPanel.add(btnClearPath);
        filePanel.add(btnOpen);
        filePanel.add(btnSave);
        //add action listener
        btnClearPath.addActionListener(this);
        btnBfs.addActionListener(this);
        btnDfs.addActionListener(this);
        btnAddEdge.addActionListener(this);
        btnAddNode.addActionListener(this);
        btnOpen.addActionListener(this);
        btnSave.addActionListener(this);

        this.addMouseListener(new ML());

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pathCnt<path.size())//this will make a cool animation when drawing the path
                    pathCnt++;
                repaint();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnAddNode){
            flgAddNode = true;
            flgAddEdge = false;
        }
        else if(e.getSource()==btnAddEdge){
            flgAddEdge = true;
            flgAddNode = false;
        }
        else if(e.getSource()==btnBfs){
            try{
                path = graph.bfs(selected);
                pathCnt = 0;
                timer.start();
            }catch (VertexNotExistException ex){JOptionPane.showMessageDialog(null,"Please select a vertex first");}
        }
        else if(e.getSource()==btnDfs){
            try{
                path = graph.dfs(selected);
                pathCnt = 0;
                timer.start();
            }catch (VertexNotExistException ex){JOptionPane.showMessageDialog(null,"Please select a vertex first");}
        }
        else if(e.getSource()==btnClearPath){
            path = new ArrayList<Edge<PaintingNode>>();
            timer.stop();
            pathCnt = 0;
        }
        else if(e.getSource()==btnSave){
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(null);
            if(returnVal!=JFileChooser.APPROVE_OPTION)
                return;
            File file = fc.getSelectedFile();
            try {
                writeObject(file);
            }catch (Exception ex){}
        }
        else if(e.getSource()==btnOpen){
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);
            if(returnVal!=JFileChooser.APPROVE_OPTION)
                return;
            String fpath = fc.getSelectedFile().getAbsolutePath();
            graph = (Graph)ReadObjectFromFile(fpath);
            nodes = graph.getAllNodes();
            path = new ArrayList<Edge<PaintingNode>>();
        }
        repaint();
    }

    private void writeObject(File f) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream
                (new FileOutputStream(f));
        out.writeObject(graph);
        out.close();
    }

    public Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void readObject(File f) throws IOException
    {
        ObjectInputStream in = new ObjectInputStream
                (new FileInputStream(f));
        try {
            while(true)
            {
                graph = (Graph) in.readObject();
            }
        }
        catch(EOFException e)
        {
        }
        catch(ClassNotFoundException e){}

        nodes = graph.getAllNodes();
        path = new ArrayList<Edge<PaintingNode>>();
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(selected!=null)
            drawSelected(g);
        for (PaintingNode node : nodes) {
                node.paint(g);//draw the nodes
                ArrayList<Edge<PaintingNode>> edges = graph.getEdgesOf(node);
                for (Edge<PaintingNode> x : edges) {//draw the edges
                    drawEdge(x,g);
                }
        }
        if (path.size() != 0)
            drawPath(g);//draw the path of the latest algorithm
    }

    private void drawEdge(Edge<PaintingNode> edge,Graphics g){
        int x1 = edge.getSrc().getX();
        int x2 = edge.getDst().getX();
        int y1 = edge.getSrc().getY();
        int y2 = edge.getDst().getY();
        g.setColor(Color.BLACK);
        g.drawLine(x1,y1,x2,y2);//draw the edge
        g.setColor(Color.PINK);
        g.drawString(""+edge.getWeight(),(x1+x2)/2,(y1+y2)/2);
    }

    private void drawSelected(Graphics g){
        g.setColor(Color.RED);
        g.drawOval(selected.getX(),selected.getY(),12,12);
    }

    private void drawPath(Graphics g) {
        for (int i = 0; i < pathCnt; i++) {
            int x1 = path.get(i).getSrc().getX()
                    ,x2 = path.get(i).getDst().getX(),
                    y1 = path.get(i).getSrc().getY(),
                    y2 = path.get(i).getDst().getY();
            g.setColor(Color.RED);
            g.drawLine(x1,y1,x2,y2);
            g.setColor(Color.PINK);
            g.drawString(""+path.get(i).getWeight(),(x1+x2)/2,(y1+y2)/2);
        }
    }


    public PaintingNode select(int x, int y) {
        for (PaintingNode n : nodes) {
            if ((n.getX() <= x + 10 && n.getX() >= x - 10) && (n.getY() <= y + 10 && n.getY() >= y - 10))
                return n;
        }
        return null;
    }

    private class ML extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (flgAddNode) {
                if (select(e.getX(), e.getY()) != null) {
                    return;//dont add nodes on top of each other
                }
                PaintingNode node;
                node = new PaintingNode(nodes.size(), e.getX(), e.getY());
                nodes.add(node);
                graph.addNode(node);
                flgAddNode = false;
            } else if (flgAddEdge) {
                if (prev == null) {
                    prev = select(e.getX(), e.getY());
                    return;
                } else if (curr == null)
                    curr = select(e.getX(), e.getY());

                try {
                    Object[] options = {"Directed", "Undirected"};
                    final int DIRECTED = 0, UNDIRECTED = 1;
                    int opt = JOptionPane.showOptionDialog(null,
                            "",
                            "",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    double weight = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter the weight of the edge"));
                    if (opt == DIRECTED)
                        graph.addDirectedEdge(prev, curr, weight);
                    else
                        graph.addUndirectedEdge(prev, curr, weight);
                    prev = null;
                    curr = null;
                    flgAddEdge = false;
                } catch (Exception ee) {
                }
            } else {
                selected = select(e.getX(), e.getY());
            }

            repaint();
        }
    }
}
