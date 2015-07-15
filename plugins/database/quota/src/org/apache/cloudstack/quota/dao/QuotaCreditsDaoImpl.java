//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.
package org.apache.cloudstack.quota.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.apache.cloudstack.quota.QuotaBalanceVO;
import org.apache.cloudstack.quota.QuotaCreditsVO;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { QuotaCreditsDao.class })
public class QuotaCreditsDaoImpl extends GenericDaoBase<QuotaCreditsVO, Long> implements QuotaCreditsDao {
    @Inject
    QuotaBalanceDao _quotaBalanceDao;

    @SuppressWarnings("deprecation")
    @Override
    public List<QuotaCreditsVO> getCredits(long accountId, long domainId, Date startDate, Date endDate) {
        SearchCriteria<QuotaCreditsVO> sc = createSearchCriteria();
        if ((startDate != null) && (endDate != null) && startDate.before(endDate)) {
            sc.addAnd("startDate", SearchCriteria.Op.BETWEEN, startDate, endDate);
            sc.addAnd("endDate", SearchCriteria.Op.BETWEEN, startDate, endDate);
        } else {
            return new ArrayList<QuotaCreditsVO>();
        }
        return listBy(sc);
    }

    @Override
    public QuotaCreditsVO saveCredits(QuotaCreditsVO credits) {
        persist(credits);
        // make an entry in the balance table
        QuotaBalanceVO bal = new QuotaBalanceVO(credits);
        _quotaBalanceDao.persist(bal);
        return credits;
    }

}
