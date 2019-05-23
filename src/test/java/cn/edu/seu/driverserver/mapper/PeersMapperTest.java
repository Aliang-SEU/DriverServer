package cn.edu.seu.driverserver.mapper;

import cn.edu.seu.driverserver.DriverserverApplication;
import cn.edu.seu.driverserver.domin.Peers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={DriverserverApplication.class})
public class PeersMapperTest {

    @Autowired
    PeersMapper peersMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    public void add() throws Exception {

        Peers peers = new Peers();
        peers.setName("1");
        peers.setGroupName("1");
        peers.setServerAddress("123");
        peers.setCreateTime("1");
        peers.setUpdateTime("1");
        System.out.println(userMapper.getUserCount());
        peersMapper.add(peers);

    }

}