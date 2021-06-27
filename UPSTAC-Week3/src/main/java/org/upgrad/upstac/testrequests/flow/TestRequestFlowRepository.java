package org.upgrad.upstac.testrequests.flow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.upgrad.upstac.testrequests.TestRequest;

import java.util.List;
import java.util.Optional;


public interface TestRequestFlowRepository extends JpaRepository<TestRequestFlow,Long> {


    @Override
	Optional<TestRequestFlow> findById(Long id);



    @Override
	void deleteById(Long id);





    List<TestRequestFlow> findByRequest(TestRequest request);


}
