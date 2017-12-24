package com.thedream.cz.myndkproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bean.QuickSort;
import com.thedream.cz.myndkproject.bean.ShellSort;
import com.thedream.cz.myndkproject.bean.Tree;
import com.thedream.cz.myndkproject.data.entity.BossLeader;
import com.thedream.cz.myndkproject.data.entity.BuilderInfo;
import com.thedream.cz.myndkproject.data.entity.BusStrategy;
import com.thedream.cz.myndkproject.data.entity.Buttons;
import com.thedream.cz.myndkproject.data.entity.GroupLoader;
import com.thedream.cz.myndkproject.data.entity.JiLiCarInfo;
import com.thedream.cz.myndkproject.data.entity.LeftCommand;
import com.thedream.cz.myndkproject.data.entity.ManagerLeader;
import com.thedream.cz.myndkproject.data.entity.PrototypeInfo;
import com.thedream.cz.myndkproject.data.entity.RightCommand;
import com.thedream.cz.myndkproject.data.entity.SingletonInfo;
import com.thedream.cz.myndkproject.data.entity.TetrisMachine;
import com.thedream.cz.myndkproject.data.entity.helper.CarFactory;
import com.thedream.cz.myndkproject.data.entity.helper.TranficCalculator;
import com.thedream.cz.myndkproject.data.entity.helper.UserLoginContext;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLinkListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_link_list);
        ButterKnife.bind(this);
        List<String> list = new ArrayList<>();
        list.add("haha");
        list.add("wuwuw");
        list.remove(0);

        findViewById(R.id.btn_tree).setOnClickListener(v -> tree());
        findViewById(R.id.btn_shell).setOnClickListener(v -> shellSort());
        findViewById(R.id.btn_quick).setOnClickListener(view -> quickSort());
        findViewById(R.id.btn_singleton).setOnClickListener(view -> singleton());
        findViewById(R.id.btn_builder).setOnClickListener(view -> builder());
        findViewById(R.id.btn_prototype).setOnClickListener(view -> prototype());
        findViewById(R.id.btn_factory).setOnClickListener(view -> factory());
        findViewById(R.id.btn_abstract).setOnClickListener(view -> abstractFactory());
        findViewById(R.id.btn_strategy).setOnClickListener(view -> strategy());

    }

    @OnClick(R.id.btn_command)
    public void command() {
        //  命令模式
        TetrisMachine tetrisMachine = new TetrisMachine();

        LeftCommand leftCommand = new LeftCommand(tetrisMachine);
        RightCommand rightCommand = new RightCommand(tetrisMachine);

        Buttons buttons = new Buttons();
        buttons.setLeftCommand(leftCommand);
        buttons.setRightCommand(rightCommand);

        buttons.toLeft();
        buttons.toRight();
    }

    @OnClick(R.id.btn_iterator)
    public void iterator() {
        //  责任链模式
        GroupLoader groupLoader = new GroupLoader();
        ManagerLeader managerLeader = new ManagerLeader();
        BossLeader bossLeader = new BossLeader();

        groupLoader.nextHandle = managerLeader;
        managerLeader.nextHandle = bossLeader;

        groupLoader.handleRequest(9999);
        Intent intent = new Intent("com.thedream.cz.myndkproject.broadcast");
        intent.putExtra("limit", 100);
        intent.putExtra("msg", "发出 广播");
        sendOrderedBroadcast(intent, null);
    }

    @OnClick(R.id.btn_status)
    public void status() {
        //  状态模式
        UserLoginContext.getInstance().forward(this);
    }

    /**
     * 策略模式
     */
    private void strategy() {
        TranficCalculator strategy = new TranficCalculator();
        strategy.setCalculateStrategy(new BusStrategy());
        PrintUtil.printCZ("策略模式: " + strategy.price(16));
    }

    /**
     * 抽象工厂模式
     */
    private void abstractFactory() {
        //  直接参考工厂模式，感觉区别不大
        JiLiCarInfo carInfo = CarFactory.getCar(JiLiCarInfo.class);
        if (carInfo != null) carInfo.show();
    }

    /**
     * 工厂模式
     */
    private void factory() {
        JiLiCarInfo carInfo = CarFactory.getCar(JiLiCarInfo.class);
        if (carInfo != null) carInfo.show();
    }

    /**
     * 原型模式
     */
    private void prototype() {
        PrototypeInfo info = new PrototypeInfo();
        info.setText("我是原始数据");
        info.addImage("第一张图片");
        info.addImage("第二张图片");
        info.show();
        PrototypeInfo newInfo = info.clone();
        newInfo.show();

        newInfo.setText("我修改一下");
        newInfo.addImage("新增的图片");
        newInfo.show();
        info.show();
    }

    /**
     * 建造者模式
     */
    private void builder() {
        BuilderInfo info = new BuilderInfo.Builder()
                .setAddress("坪洲")
                .setAge(11)
                .setName("cz")
                .create();
        info.show();
    }

    /**
     * 单例模式
     */
    private void singleton() {
        SingletonInfo.getInstance().show();
    }

    private void quickSort() {
        int maxSize = 16;
        QuickSort sort = new QuickSort(maxSize);

        for (int j = 0; j < maxSize; j++) {
            sort.insert((long) (Math.random() * 99));
        }

        sort.diplay();
        sort.quickSort();
        sort.diplay();
    }

    private void shellSort() {
        int maxSize = 10;
        ShellSort shellSort = new ShellSort(maxSize);

        for (int j = 0; j < maxSize; j++) {
            shellSort.insert((long) (Math.random() * 99));
        }
        shellSort.diplay();
        shellSort.shellSort();
        shellSort.diplay();
    }

    private void tree() {
        Tree tree = new Tree();

        tree.insert(50, 1.5);
        tree.insert(25, 1.1);
        tree.insert(75, 1.7);
        tree.insert(12, 1.3);
        tree.insert(37, 1.2);
        tree.insert(44, 1.9);
        tree.insert(30, 2.0);
        tree.insert(33, 2.2);
        tree.insert(87, 1.4);
        tree.insert(77, 1.1);
        tree.insert(97, 1.8);
        tree.insert(85, 0.9);
        tree.insert(100, 2.3);

        tree.inOrder();

        PrintUtil.printCZ("删除值:" + tree.delete(75));

        tree.inOrder();

        PrintUtil.printCZ("查找30的值:" + tree.find(30));
    }
}
