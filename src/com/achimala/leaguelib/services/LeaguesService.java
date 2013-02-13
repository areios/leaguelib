/*
 *  This file is part of LeagueLib.
 *  LeagueLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LeagueLib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LeagueLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.achimala.leaguelib.services;

import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;
import com.gvaneyck.rtmp.TypedObject;

public class LeaguesService extends LeagueAbstractService {
    public LeaguesService(LeagueConnection connection) {
        super(connection);
    }
    
    public String getServiceName() {
        return "leaguesServiceProxy";
    }
    
    public void fillSoloQueueLeagueData(LeagueSummoner summoner) throws LeagueException {
        TypedObject obj = call("getLeagueForPlayer", new Object[] { summoner.getId(), LeagueMatchmakingQueue.RANKED_SOLO_5x5.toString() });
        summoner.setLeagueStats(new LeagueSummonerLeagueStats(obj.getTO("body")));
    }
    
    public void fillSoloQueueLeagueData(final LeagueSummoner summoner, final Callback<LeagueSummoner> callback) {
        callAsynchronously("getLeagueForPlayer", new Object[] { summoner.getId(), LeagueMatchmakingQueue.RANKED_SOLO_5x5.toString() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    summoner.setLeagueStats(new LeagueSummonerLeagueStats(obj.getTO("body")));
                    callback.onCompletion(summoner);
                } catch(LeagueException ex) {
                    callback.onError(ex);
                }
            }
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}